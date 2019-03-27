package vote.model.rankedCalculators;

import vote.model.entities.Ballot;
import vote.model.entities.Selection;
import vote.model.entities.Slate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RankedCalculatorBase {

    Slate generateTestSlate() {
        Slate slate1 = new Slate("Breakfast Foods", "Jordan", new java.util.Date());
        slate1.addSelection(new Selection("Eggs", "Scrambled", 1, slate1));
        slate1.addSelection(new Selection("Bacon", "crispy", 2, slate1));
        slate1.addSelection(new Selection("Cereal", "just regular cereal...", 3, slate1));
        slate1.addSelection(new Selection("Fruit", "Probably healthy", 4, slate1));
        slate1.addSelection(new Selection("Whiskey", "If you had a hard night", 5, slate1));

        return slate1;
    }

    Ballot ballotGenerator(Slate slate1, String voter, List<Integer> order) {
        Ballot ballot1 = new Ballot(voter, slate1);
        ballot1.rankingOrder(order);
        return ballot1;
    }

    List<Ballot> generateTestBallots() {
        Slate slate1 = generateTestSlate();
        List<Ballot> ballots = new ArrayList<>();

        ballots.add(ballotGenerator(slate1, "John", Arrays.asList(4,2,3,1,5)));
        ballots.add(ballotGenerator(slate1, "Jane", Arrays.asList(2,5,1,3,4)));
        ballots.add(ballotGenerator(slate1, "Michelle", Arrays.asList(5,2,3,1,4)));
        ballots.add(ballotGenerator(slate1, "Franklin", Arrays.asList(1,4,2,3,5)));
        ballots.add(ballotGenerator(slate1, "Trillian", Arrays.asList(3,2,1,5,4)));
        ballots.add(ballotGenerator(slate1, "Ford", Arrays.asList(3,4,5,2,1)));
        ballots.add(ballotGenerator(slate1, "Zane", Arrays.asList(2,4,5,3,1)));

        return ballots;
    }
}
