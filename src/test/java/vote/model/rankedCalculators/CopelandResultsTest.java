package vote.model.rankedCalculators;

import org.junit.Test;
import vote.model.rankingCalculators.CopelandResults;
import org.junit.Assert;

public class CopelandResultsTest extends RankedCalculatorBase{
    @Test
    public void CopelandTest1() {
        CopelandResults results = new CopelandResults(generateTestBallots());
        System.out.println(results);

        for(CopelandResults.SelectionResults sr : results.getSelectionResults()) {
            if(sr.getSelection().getName().equals("Eggs")) {
                Assert.assertEquals(1, sr.getWins());
            }
            if(sr.getSelection().getName().equals("Cereal")) {
                Assert.assertEquals(3, sr.getWins());
            }
        }
        Assert.assertEquals("Bacon", results.getWinner().getName());
    }
}

//4,2,3,1,5
//2,5,1,3,4
//5,2,3,1,4
//1,4,2,3,5
//3,2,1,5,4
//3,4,5,2,1
//2,4,5,3,1

//combination : record of which is first : wins for each : winner

//1-2 : 2,2,2,1,2,2,2 : 1-6 : 2 wins
//1-3 : 3,1,3,1,3,3,3 : 2-5 : 3 wins
//1-4 : 4,1,1,1,1,4,4 : 4-3 : 1 wins
//1-5 : 1,5,5,1,1,5,5 : 3-4 : 5 wins
//2-3 : 2,2,2,2,3,3,2 : 5-2 : 2 wins
//2-4 : 4,2,2,4,2,4,2 : 4-3 : 2 wins
//2-5 : 2,2,5,2,2,5,2 : 5-2 : 2 wins
//3-4 : 4,3,3,4,3,3,4 : 4-3 : 3 wins
//3-5 : 3,5,5,3,3,3,5 : 4-3 : 3 wins
//4-5 : 4,5,5,4,5,4,4 : 4-3 : 4 wins

//1 wins 1 time
//2 wins 4 times
//3 wins 3 times
//4 wins 1 time
//5 wins 1 time

//2 wins overall
