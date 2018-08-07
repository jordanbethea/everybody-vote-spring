package vote.model.rankedCalculators;

import org.junit.Test;
import org.junit.Assert;
import vote.model.rankingCalculators.BordaCount;
import vote.model.rankingCalculators.BordaCount.SelectionResults;
import java.math.BigDecimal;

public class BordaCountTest extends RankedCalculatorBase{
    @Test
    public void testBordaCountBase1() {
        BordaCount base1Count = new BordaCount(BordaCount.COUNT_TYPE.BASE_1, generateTestBallots());
        System.out.println(base1Count);
        Assert.assertEquals("Bacon", base1Count.getWinningSelection().getName());
        for(SelectionResults sr : base1Count.getSelectionResults()) {
            if(sr.getSelection().getPosition() == 1){ Assert.assertEquals(new BigDecimal(17), sr.getTotalScore());}
            if(sr.getSelection().getPosition() == 2){ Assert.assertEquals(new BigDecimal(27), sr.getTotalScore());}
            if(sr.getSelection().getPosition() == 3){ Assert.assertEquals(new BigDecimal(22), sr.getTotalScore());}
            if(sr.getSelection().getPosition() == 4){ Assert.assertEquals(new BigDecimal(20), sr.getTotalScore());}
            if(sr.getSelection().getPosition() == 5){ Assert.assertEquals(new BigDecimal(19), sr.getTotalScore());}
        }
    }

    @Test
    public void testBordaCountBase0() {
        BordaCount base1Count = new BordaCount(BordaCount.COUNT_TYPE.BASE_0, generateTestBallots());
        System.out.println(base1Count);
        Assert.assertEquals("Bacon", base1Count.getWinningSelection().getName());
        for(SelectionResults sr : base1Count.getSelectionResults()) {
            if(sr.getSelection().getPosition() == 1){ Assert.assertEquals(new BigDecimal(10), sr.getTotalScore());}
            if(sr.getSelection().getPosition() == 2){ Assert.assertEquals(new BigDecimal(20), sr.getTotalScore());}
            if(sr.getSelection().getPosition() == 3){ Assert.assertEquals(new BigDecimal(15), sr.getTotalScore());}
            if(sr.getSelection().getPosition() == 4){ Assert.assertEquals(new BigDecimal(13), sr.getTotalScore());}
            if(sr.getSelection().getPosition() == 5){ Assert.assertEquals(new BigDecimal(12), sr.getTotalScore());}
        }
    }

    private final BigDecimal finalResult1 = (new BigDecimal(2.566666)).setScale(6, BigDecimal.ROUND_HALF_EVEN);
    private final BigDecimal finalResult2 = (new BigDecimal(4.083333)).setScale(6, BigDecimal.ROUND_HALF_EVEN);
    private final BigDecimal finalResult3 = (new BigDecimal(3.416666)).setScale(6, BigDecimal.ROUND_HALF_EVEN);
    private final BigDecimal finalResult4 = (new BigDecimal(3.1)).setScale(6, BigDecimal.ROUND_HALF_EVEN);
    private final BigDecimal finalResult5 = (new BigDecimal(2.816666)).setScale(6, BigDecimal.ROUND_HALF_EVEN);

    @Test
    public void testBordaCountDowdall() {
        BordaCount base1Count = new BordaCount(BordaCount.COUNT_TYPE.DOWDALL, generateTestBallots());
        System.out.println(base1Count);
        Assert.assertEquals("Bacon", base1Count.getWinningSelection().getName());
        for(SelectionResults sr : base1Count.getSelectionResults()) {
            if(sr.getSelection().getPosition() == 1){ Assert.assertEquals(finalResult1, sr.getTotalScore());}
            if(sr.getSelection().getPosition() == 2){ Assert.assertEquals(finalResult2, sr.getTotalScore());}
            if(sr.getSelection().getPosition() == 3){ Assert.assertEquals(finalResult3, sr.getTotalScore());}
            if(sr.getSelection().getPosition() == 4){ Assert.assertEquals(finalResult4, sr.getTotalScore());}
            if(sr.getSelection().getPosition() == 5){ Assert.assertEquals(finalResult5, sr.getTotalScore());}
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

//Base 1
//1 : 2 + 3 + 2 + 5 + 3 + 1 + 1 = 17
//2 : 4 + 5 + 4 + 3 + 4 + 2 + 5 = 27
//3 : 3 + 2 + 3 + 2 + 5 + 5 + 2 = 22
//4 : 5 + 1 + 1 + 4 + 1 + 4 + 4 = 20
//5 : 1 + 4 + 5 + 1 + 2 + 3 + 3 = 19
//2 wins

//Base 0
//1 : 1 + 2 + 1 + 4 + 2 + 0 + 0 = 10
//2 : 3 + 4 + 3 + 2 + 3 + 1 + 4 = 20
//3 : 2 + 1 + 2 + 1 + 4 + 4 + 1 = 15
//4 : 4 + 0 + 0 + 3 + 0 + 3 + 3 = 13
//5 : 0 + 3 + 4 + 0 + 1 + 2 + 2 = 12
//2 wins

//Dowdall
//1 : 1/4 + 1/3 + 1/4 + 1/1 + 1/3 + 1/5 + 1/5
// base 60 : 15/60 + 20/60 + 15/60 + 60/60 + 20/60 + 12/60 + 12/60 =
//  15 + 20 + 15 + 60 + 20 + 12 + 12 = 154/60 = 2.566666

//2 : 1/2 + 1/1 + 1/2 + 1/3 + 1/2 + 1/4 + 1/1
// base 60 : 30 + 60 + 30 + 20 + 30 + 15 + 60 = 245/60 = 4.083333

//3 : 1/3 + 1/4 + 1/3 + 1/4 + 1/1 + 1/1 + 1/4
//base 60 : 20 + 15 + 20 + 15 + 60 + 60 + 15 = 205/60 = 3.416666

//4 : 1/1 + 1/5 + 1/5 + 1/2 + 1/5 + 1/2 + 1/2
//base 60 : 60 + 12 + 12 + 30 + 12 + 30 + 30 = 186/60 = 3.1

//5 : 1/5 + 1/2 + 1/1 + 1/5 + 1/4 + 1/3 + 1/3
//base 60 : 12 + 30 + 60 + 12 + 15 + 20 + 20 = 169/60 = 2.816666