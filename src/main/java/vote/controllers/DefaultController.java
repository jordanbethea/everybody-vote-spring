package vote.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class DefaultController {
    private static final Logger log = LoggerFactory.getLogger(DefaultController.class);

    @RequestMapping
    public String homePage() {
        return "index";
    }

}
