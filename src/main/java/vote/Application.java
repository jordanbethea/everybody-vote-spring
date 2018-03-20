package vote;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import vote.repositories.SelectionRepository;
import vote.repositories.SlateRepository;
import vote.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx, SelectionRepository selRepo, SlateRepository slateRepo) {
        return args -> {

            System.out.println("Let's inspect the beans provided by Spring Boot:");
            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }

            log.info("Load some test data into repos:");
            Slate slate1 = new Slate("How would you like the world to end?", "Jordan", new java.util.Date());
            Selection sel1 = new Selection("Fire", "Very hot", 0, slate1);
            Selection sel2 = new Selection("Ice", "very cold", 1, slate1);
            slate1.addSelection(sel1);
            slate1.addSelection(sel2);

            slateRepo.save(slate1);
            selRepo.save(sel1);
            selRepo.save(sel2);


            log.info("Saved data, now lets load");
            Slate testSlate = slateRepo.findOne(1L);
            if(testSlate == null){ log.info("Slate was null");}
            log.info(testSlate.toString());

            Selection testSel = selRepo.findOne(2L);
            if(testSel == null){ log.info("Selection was null");}
            log.info(testSel.toString());

        };
    }

}
