package vote.model;

import org.junit.Test;
import org.junit.Assert;
import vote.model.*;
import vote.model.IRVResults.RoundResults;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class IRVResultsTest {

    @Test
    public void IRVTest1() {
        IRVResults results = new IRVResults(generateTestBallots());

        for(RoundResults round : results.roundResults) {
            if(round.round == 1) {
                Assert.assertEquals("Eggs", round.getLowest().getSelection().getName());
                Assert.assertEquals(1, round.getLowest().getCount());
            }
            if(round.round == 3) {
                Assert.assertNull(round.getWinner());
                Assert.assertEquals("Cereal", round.getLowest().getSelection().getName());
            }
        }
        Assert.assertEquals("Bacon", results.overallWinner.getName());
    }

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
    // selection: count
    //Round 1 - 1:1, 2:2, 3:2, 4:1, 5:1 - 1 loses, is first in tie for last
    //4,2,3,5
    //2,5,3,4
    //5,2,3,4
    //4,2,3,5
    //3,2,5,4
    //3,4,5,2
    //2,4,5,3
    //Round 2 - 2:2, 3:2, 4:2, 5:1 - 5 loses, is in last place
    //4,2,3
    //2,3,4
    //2,3,4
    //4,2,3
    //3,2,4
    //3,4,2
    //2,4,3
    //Round 3 - 2:3, 3:2, 4:2 - 3 loses, is first in tie for last
    //4,2
    //2,4
    //2,4
    //4,2
    //2,4
    //4,2
    //2,4
    //Round 4 - 2:4, 4:3 - 2 wins, is over 50%