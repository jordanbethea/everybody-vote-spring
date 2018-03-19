package vote.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class VoteController {

    @RequestMapping("/")
    public String index() {
        return "Get ready to vote on stuff";
    }

}
