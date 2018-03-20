package vote.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import vote.repositories.*;

@RestController
@RequestMapping("/slate")
public class SlateController{

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

}
