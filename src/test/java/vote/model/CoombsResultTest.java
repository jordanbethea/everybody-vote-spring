package vote.model;

import org.junit.Test;
import org.junit.Assert;
import vote.model.*;
import vote.model.IRVResults.RoundResults;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class CoombsResultTest {

    @Test
    public void testCoombsResults1() {
        CoombsResults results = new CoombsResults(generateTestBallots());
        System.out.println(results.toString());

        Assert.assertEquals("Bacon", results.getWinner().getName());
        for(CoombsResults.RoundResults roundResult : results.getRoundResults()) {
            if(roundResult.getRound() == 1) {
                Assert.assertEquals("Fruit", roundResult.getLoser().getSelection().getName());
            }
            if(roundResult.getRound() == 2) {
                Assert.assertEquals("Eggs", roundResult.getLoser().getSelection().getName());
            }
            if(roundResult.getRound() == 3) {
                Assert.assertEquals("Cereal", roundResult.getLoser().getSelection().getName());
                Assert.assertEquals("Bacon", roundResult.getWinner().getSelection().getName());
            }
        }
    }

    //TODO at some point pull this out into standalone ranked test data generation for all calculators
    private Slate generateTestSlate() {
        Slate slate1 = new Slate("Breakfast Foods", "Jordan", new java.util.Date());
        slate1.addSelection(new Selection("Eggs", "Scrambled", 1, slate1));
        slate1.addSelection(new Selection("Bacon", "crispy", 2, slate1));
        slate1.addSelection(new Selection("Cereal", "just regular cereal...", 3, slate1));
        slate1.addSelection(new Selection("Fruit", "Probably healthy", 4, slate1));
        slate1.addSelection(new Selection("Whiskey", "If you had a hard night", 5, slate1));

        return slate1;
    }

    private Ballot ballotGenerator(Slate slate1, String voter, List<Integer> order) {
        Ballot ballot1 = new Ballot(voter, slate1);
        ballot1.rankingOrder(order);
        return ballot1;
    }

    private List<Ballot> generateTestBallots() {
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

//4,2,3,1,5
//2,5,1,3,4
//5,2,3,1,4
//1,4,2,3,5
//3,2,1,5,4
//3,4,5,2,1
//2,4,5,3,1
//Round 1
    //highest - 1:1, 2:2, 3:2, 4:1, 5:1
    //lowest - 1:2, 2:0, 3:0, 4:3, 5:2
    //no winner, 4 is loser
//2,3,1,5
//2,5,1,3
//5,2,3,1
//1,2,3,5
//3,2,1,5
//3,5,2,1
//2,5,3,1
//Round 2
    //highest - 1:1, 2:3, 3:2, 5:1
    //lowest - 1:3, 2:0, 3:1, 5:3
    //no winner, 1 is first loser
//2,3,5
//2,5,3
//5,2,3
//2,3,5
//3,2,5
//3,5,2
//2,5,3
//Round 3
    //highest - 2:4, 3:1, 5:1
    //lowest - 2:1, 3:3, 5:3
    //2 is the winner