package vote;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import vote.model.entities.Ballot;
import vote.model.entities.RankedChoice;
import vote.model.entities.Selection;
import vote.model.entities.Slate;
import vote.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx, SelectionRepository selRepo, SlateRepository slateRepo,
                                               BallotRepository ballotRepo, RankedChoiceRepository rankRepo) {
        return args -> {

            System.out.println("Let's inspect the beans provided by Spring Boot:");
            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }

            log.info("Load some test data into repos:");
            Slate slate1 = new Slate("How would you like the world to end?", "Jordan", new java.util.Date());
            Selection sel1 = new Selection("Fire", "Very hot", 1, slate1);
            Selection sel2 = new Selection("Ice", "very cold", 2, slate1);
            Selection sel3 = new Selection("Acid", "stings", 3, slate1);
            Selection sel4 = new Selection("Electricity", "also stings", 4, slate1);
            slate1.addSelection(sel1);
            slate1.addSelection(sel2);
            slate1.addSelection(sel3);
            slate1.addSelection(sel4);

            slateRepo.save(slate1);
            selRepo.save(sel1);
            selRepo.save(sel2);
            selRepo.save(sel3);
            selRepo.save(sel4);

            Ballot bal1 = new Ballot("JB", slate1);
            ballotRepo.save(bal1);

            RankedChoice rc1 = new RankedChoice(sel3, 1, bal1);
            RankedChoice rc2 = new RankedChoice(sel1, 2, bal1);
            RankedChoice rc3 = new RankedChoice(sel2, 3, bal1);
            RankedChoice rc4 = new RankedChoice(sel4, 4, bal1);
            bal1.setRankedChoices(Arrays.asList(rc1, rc2, rc3, rc4));

            rankRepo.save(rc1);
            rankRepo.save(rc2);
            rankRepo.save(rc3);
            rankRepo.save(rc4);

            Ballot bal2 = new Ballot("AG", slate1);
            ballotRepo.save(bal2);
            bal2.rankingOrder(Arrays.asList(2,4,1,3));
            for(RankedChoice rc : bal2.getRankedChoices()){rankRepo.save(rc);}

            Ballot bal3 = new Ballot("DH", slate1);
            ballotRepo.save(bal3);
            bal3.rankingOrder(Arrays.asList(1,3,4,2));
            for(RankedChoice rc : bal3.getRankedChoices()){rankRepo.save(rc);}



            log.info("Saved data, now lets load");
            Slate testSlate = slateRepo.findById(1L).orElse(null);
            if(testSlate == null){ log.info("Slate was null");}
            log.info(testSlate.toString());

            Selection testSel = selRepo.findById(2L).orElse(null);
            if(testSel == null){ log.info("Selection was null");}
            log.info(testSel.toString());

        };
    }

}
