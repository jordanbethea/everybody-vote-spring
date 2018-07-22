package vote.calculators;

import vote.model.Ballot;
import vote.model.IRVResults;

import java.util.Date;
import java.util.List;


public class InstantRunoffCalculator {

    List<Ballot> ballots;

    public InstantRunoffCalculator(List<Ballot> ballots) {
        this.ballots = ballots;
    }

    public static IRVResults generateResults(List<Ballot> ballots) {
        IRVResults results = new IRVResults();



        return results;
    }
}

