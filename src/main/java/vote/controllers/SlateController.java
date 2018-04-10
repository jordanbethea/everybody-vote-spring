package vote.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.view.RedirectView;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.validation.BindingResult;

import org.springframework.ui.Model;

import vote.repositories.*;
import vote.model.Slate;
import vote.model.Selection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/slate")
public class SlateController{
    private static final Logger log = LoggerFactory.getLogger(SlateController.class);


    private final SelectionRepository selRepository;
    private final SlateRepository slateRepository;

    @Autowired
    public SlateController(SelectionRepository selectionRepository, SlateRepository slateRepository){
        this.selRepository = selectionRepository;
        this.slateRepository = slateRepository;
    }

    @RequestMapping()
    public String showSlates(Model model){
        model.addAttribute("slates", slateRepository.findAll());
        return "slateList";
    }

    @RequestMapping(value = "/new", method=RequestMethod.GET)
    public String newSlate(Model model) {
        Slate newSlate = new Slate();
        newSlate.addSelection(new Selection());
        newSlate.addSelection(new Selection());
        newSlate.addSelection(new Selection());
        model.addAttribute("newSlate", newSlate);

        return "newSlate";
    }

    @RequestMapping(value="/create", params={"addRow"})
    public String addSelection(@ModelAttribute Slate newSlate,final BindingResult bindingResult, Model model) {
        newSlate.getSelections().add(new Selection());
        model.addAttribute("newSlate", newSlate);
        return "newSlate";
    }

    @RequestMapping(value="/create", params={"removeRow"})
    public String removeSelection(@ModelAttribute Slate newSlate, final BindingResult bindingResult,
            final HttpServletRequest req, Model model) {
        int toRemove = new Integer(req.getParameter("removeRow"));
        log.info("Removing Row "+toRemove+" from Slate "+newSlate.getTopic()+", num selections: "+newSlate.getSelections().size());
        Selection removed = newSlate.getSelections().remove(toRemove);
        log.info("Removed, now is length: "+newSlate.getSelections().size());
        model.addAttribute("newSlate", newSlate);
        return "newSlate";
    }

    @RequestMapping(value="/create", params={"rowUp"})
    public String moveSelectionUp(@ModelAttribute Slate newSlate, final BindingResult bindingResult,
            final HttpServletRequest req, Model model) {
        int position = new Integer(req.getParameter("rowUp"));
        if(position > 0 && position <= newSlate.getSelections().size() - 1) {
            Selection toMove = newSlate.getSelections().remove(position);
            newSlate.getSelections().add(position - 1, toMove);
        }
        model.addAttribute("newSlate", newSlate);
        return "newSlate";
    }

    @RequestMapping(value="/create", params={"rowDown"})
    public String moveSelectionDown(@ModelAttribute Slate newSlate, final BindingResult bindingResult,
                                  final HttpServletRequest req, Model model) {
        int position = new Integer(req.getParameter("rowDown"));
        if(position >= 0 && position < newSlate.getSelections().size() - 1) {
            Selection toMove = newSlate.getSelections().remove(position);
            newSlate.getSelections().add(position + 1, toMove);
        }
        model.addAttribute("newSlate", newSlate);
        return "newSlate";
    }

    @RequestMapping(value="/create", method=RequestMethod.POST)
    public RedirectView createSlate(@ModelAttribute Slate newSlate) {
        log.info("Running create slate function. Has "+newSlate.getSelections().size()+" selections");

        slateRepository.save(newSlate);
        for(Selection s: newSlate.getSelections()) {
            log.info("Slate has selection: "+s.toString());
            s.setSlate(newSlate);
            selRepository.save(s);
        }

        return new RedirectView("/slate/show/"+newSlate.getId());
    }

    @RequestMapping(value="/show/{id}", method=RequestMethod.GET)
    public String showSlate(@PathVariable String id, Model model) {
        Long longId = new Long(id);
        Slate toView = slateRepository.findOne(longId);
        model.addAttribute("slate", toView);

        return "viewSlate";
    }

}
