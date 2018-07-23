package vote.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import vote.model.*;
import vote.model.IRVResults.RoundResults;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class BallotTest {

    Slate testSlate;

    @Before
    public void setup() {
        testSlate = new Slate("Food", "Jordan", new Date());
        testSlate.addSelection(new Selection("Pizza", "Good", 1, testSlate));
        testSlate.addSelection(new Selection("Fries", "ok", 2, testSlate));
        testSlate.addSelection(new Selection("Broccoli", "healthy", 3, testSlate));
        testSlate.addSelection(new Selection("Water", "necessary", 4, testSlate));
    }

    @Test
    public void RankingOrderTest() {
        Ballot ballot1 = new Ballot("Jordan", testSlate);
        ballot1.rankingOrder(Arrays.asList(2,3,4,1));
        Assert.assertEquals(1, ballot1.getRankedChoices().get(0).getRanking());
        Assert.assertEquals(2, ballot1.getRankedChoices().get(0).getSelection().getPosition());
        Assert.assertEquals(2, ballot1.getRankedChoices().get(1).getRanking());
        Assert.assertEquals(3, ballot1.getRankedChoices().get(1).getSelection().getPosition());
        Assert.assertEquals(3, ballot1.getRankedChoices().get(2).getRanking());
        Assert.assertEquals(4, ballot1.getRankedChoices().get(2).getSelection().getPosition());
    }
}
