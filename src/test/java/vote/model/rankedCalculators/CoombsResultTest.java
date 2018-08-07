package vote.model.rankedCalculators;

import org.junit.Test;
import org.junit.Assert;
import vote.model.rankingCalculators.CoombsResults;

public class CoombsResultTest extends RankedCalculatorBase{

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