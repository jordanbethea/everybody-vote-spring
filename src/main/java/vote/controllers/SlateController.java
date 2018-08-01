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
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.ui.Model;

import vote.model.*;
import vote.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/slate")
public class SlateController{
    private static final Logger log = LoggerFactory.getLogger(SlateController.class);

    private final SelectionRepository selRepository;
    private final SlateRepository slateRepository;
    private final BallotRepository ballotRepository;

    @Autowired
    public SlateController(SelectionRepository selectionRepository,
                           SlateRepository slateRepository, BallotRepository ballotRepository){
        this.selRepository = selectionRepository;
        this.slateRepository = slateRepository;
        this.ballotRepository = ballotRepository;
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

        List<Ballot> ballots = ballotRepository.findByVotedSlate(toView);
        model.addAttribute("ballots", ballots);

        return "viewSlate";
    }

    @RequestMapping(value="results/{id}", method=RequestMethod.GET)
    public String showSlateResults(@PathVariable String id, Model model) {
        Long longId = new Long(id);
        Slate toView = slateRepository.findOne(longId);
        List<Ballot> ballots = ballotRepository.findByVotedSlate(toView);

        model.addAttribute("slate", toView);
        model.addAttribute("ballots", ballots);

        Map<Selection, Integer> FPTPresults = new HashMap<Selection, Integer>();
        for(Ballot b : ballots) {
            Selection s = b.getSingleVoteChoice();
            Integer count = FPTPresults.get(s);
            if(count == null){ count = 0;}
            count++;
            FPTPresults.put(s, count);
        }
        model.addAttribute("FPTP", FPTPresults);

        IRVResults irv = new IRVResults(ballots);
        log.debug(irv.toString());

        model.addAttribute("irvResults", irv);

        CoombsResults coombsResults = new CoombsResults(ballots);
        model.addAttribute("coombsResults", coombsResults);

        CopelandResults copelandResults = new CopelandResults(ballots);
        model.addAttribute("copelandResults", copelandResults);

        return "viewSlateResults";
    }

}
