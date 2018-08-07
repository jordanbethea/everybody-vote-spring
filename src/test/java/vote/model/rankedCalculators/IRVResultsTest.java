package vote.model.rankedCalculators;

import org.junit.Test;
import org.junit.Assert;
import vote.model.rankingCalculators.IRVResults;
import vote.model.rankingCalculators.IRVResults.RoundResults;

public class IRVResultsTest extends RankedCalculatorBase{

    @Test
    public void IRVTest1() {
        IRVResults results = new IRVResults(generateTestBallots());

        for(RoundResults round : results.getRoundResults()) {
            if(round.getRound() == 1) {
                Assert.assertEquals("Eggs", round.getLowest().getSelection().getName());
                Assert.assertEquals(1, round.getLowest().getCount());
            }
            if(round.getRound() == 3) {
                Assert.assertNull(round.getWinner());
                Assert.assertEquals("Cereal", round.getLowest().getSelection().getName());
            }
        }
        Assert.assertEquals("Bacon", results.getOverallWinner().getName());
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