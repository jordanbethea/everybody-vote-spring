package vote.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.view.RedirectView;
import vote.repositories.SelectionRepository;
import vote.repositories.SlateRepository;
import vote.repositories.BallotRepository;
import vote.repositories.RankedChoiceRepository;
import vote.model.RankedChoice;
import org.springframework.validation.BindingResult;
import javax.servlet.http.HttpServletRequest;


import org.springframework.ui.Model;

import vote.model.Ballot;
import vote.model.Slate;

@Controller
@RequestMapping("/vote")
public class VoteController {

    private final SelectionRepository selRepository;
    private final SlateRepository slateRepository;
    private final BallotRepository ballotRepository;
    private final RankedChoiceRepository rankRepository;

    @Autowired
    public VoteController(SelectionRepository selRepository, SlateRepository slateRepository,
                          BallotRepository ballotRepository, RankedChoiceRepository rankRepository) {
        this.selRepository = selRepository;
        this.slateRepository = slateRepository;
        this.ballotRepository = ballotRepository;
        this.rankRepository = rankRepository;
    }

    @RequestMapping(value="/{slateIDs}", method= RequestMethod.GET)
    public String newVote(@PathVariable String slateIDs, Model model) {
        Long slateID = new Long(slateIDs);
        Slate chosenSlate = slateRepository.findOne(slateID);
        model.addAttribute("slate", chosenSlate);
        Ballot newBallot = new Ballot();
        newBallot.setVotedSlate(chosenSlate);
        model.addAttribute("ballot", newBallot);

        return "votingForm";
    }

    @RequestMapping(value="/{slateIDs}/create", params={"rankDown"})
    public String rankDown(@PathVariable String slateIDs, @ModelAttribute Ballot newBallot, final BindingResult bindingResult,
            final HttpServletRequest req, Model model) {
        Long slateID = new Long(slateIDs);
        Slate chosenSlate = slateRepository.findOne(slateID);
        model.addAttribute("slate", chosenSlate);
        int ranking = new Integer(req.getParameter("rankDown"));
        newBallot.rankVoteDown(ranking);
        model.addAttribute("ballot", newBallot);
        return "votingForm";
    }

    @RequestMapping(value="/{slateIDs}/create", params={"rankUp"})
    public String rankUp(@PathVariable String slateIDs, @ModelAttribute Ballot newBallot, final BindingResult bindingResult,
                           final HttpServletRequest req, Model model) {
        Long slateID = new Long(slateIDs);
        Slate chosenSlate = slateRepository.findOne(slateID);
        model.addAttribute("slate", chosenSlate);
        int ranking = new Integer(req.getParameter("rankUp"));
        newBallot.rankVoteUp(ranking);
        model.addAttribute("ballot", newBallot);
        return "votingForm";
    }

    @RequestMapping(value="/{slateIDs}/create", method=RequestMethod.POST)
    public RedirectView castVote(@PathVariable String slateIDs, @ModelAttribute Ballot newBallot, Model model) {
        Long slateID = new Long(slateIDs);
        Slate chosenSlate = slateRepository.findOne(slateID);
        newBallot.setVotedSlate(chosenSlate);

        ballotRepository.save(newBallot);
        for(RankedChoice choice : newBallot.getRankedChoices()) {
            choice.setBallot(newBallot);
            rankRepository.save(choice);
        }

        return new RedirectView("/slate/show/"+chosenSlate.getId());
    }

    /*@RequestMapping("/")
    public String index() {
        return "Get ready to vote on stuff";
    }*/

}
