package vote.model.rankingCalculators;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vote.model.Ballot;
import vote.model.RankedChoice;
import vote.model.Selection;
import vote.model.Slate;

/**
 * Instant runoff election - used with ranked voting
 * Checks all top ranked results, calculates FPTP winner from them
 * If no one has over 50% of votes, remove lowest vote getter from contention. Calculate again.
 * Continue until you find a winner or only one candidate remaining
 */


public class IRVResults {
    private static final Logger log = LoggerFactory.getLogger(IRVResults.class);

    Slate slate;
    int totalVotes;
    Date countDate;
    Selection overallWinner = null;
    List<RoundResults> roundResults;

    public List<RoundResults> getRoundResults(){ return roundResults;}
    public Selection getOverallWinner(){ return overallWinner;}

    public String toString() {
        String format = "Slate topic: %s, total votes: %d %nRound Results: %n%s";
        return String.format(format, slate.getTopic(), totalVotes, roundResults.toString());
    }

    public IRVResults(List<Ballot> ballots) {
        roundResults = new ArrayList<>();
        int round = 0;
        boolean winner = false;
        totalVotes = ballots.size();
        if(ballots.size() > 0){ slate = ballots.get(0).getVotedSlate();}
        else return;
        //make copy of ranked results so they can be manipulated apart from the raw results
        List<List<RankedChoice>> allRankedResults = new ArrayList<List<RankedChoice>>();
        for(Ballot b : ballots) {
            List<RankedChoice> rankedChoices = new ArrayList<RankedChoice>();
            rankedChoices.addAll(b.getRankedChoices());
            Collections.sort(rankedChoices);
            allRankedResults.add(rankedChoices);
        }
        List<Selection> availableSelections = new ArrayList<>();
        availableSelections.addAll(slate.getSelections());
        log.info("Initial available selections: "+availableSelections.toString());

        while(!winner) {
            round++;
            List<Selection> newAvailableSelections = new ArrayList<>();
            newAvailableSelections.addAll(availableSelections);
            RoundResults results = new RoundResults(round,newAvailableSelections);
            //If there is only one option remaining, that option is the winner by default

            for (List<RankedChoice> singleRankedVote : allRankedResults) {
                //singleRankedVote.sort(Comparator.comparing(RankedChoice::getRanking));
                //take first/highest ranked vote, add to vote total
                results.addVote(singleRankedVote.get(0).getSelection()); //results should be ordered by here
            }
            results.evaluateRound();
            roundResults.add(results);
            if(results.getWinner() != null){
                winner = true;
                overallWinner = results.getWinner().getSelection();
            } else {
                availableSelections.remove(results.getLowest().getSelection());
                for(List<RankedChoice> singleRankedVote: allRankedResults) {
                    RankedChoice match = null;
                    for(RankedChoice rc : singleRankedVote) {
                        if(rc.getSelection().equals(results.getLowest().getSelection())) {
                            match = rc;
                            break;
                        }
                    }
                    singleRankedVote.remove(match);
                }
            }
            //take all vote totals, find highest count total
            //check if that total is more than 50% of all votes cast
            //If so, mark winner, return results

            //if not, find lowest count total among all options (if tie, uses first in order)
            //remove that option from all ballot data
        }
    }

    public class RoundResults {
        int round;
        int totalVotes;
        List<Selection> remainingSelections;
        ResultCount winner = null;
        ResultCount lowest = null;
        Map<Selection, ResultCount> allResults;

        public int getRound(){ return round; }
        public List<Selection> getRemainingSelections(){ return remainingSelections;}
        public ResultCount getWinner(){ return winner;}
        public ResultCount getLowest(){ return lowest;}
        public Map<Selection, ResultCount> getAllResults(){ return allResults; }
        public ResultCount getResultForSelection(Selection s){ return allResults.get(s);}

        public String toString() {
            String format = "Round %d of voting. Remaining selections: %n%s %nFull Results:%n%s%nWinner: %s%nLoser:%n%s";
            return String.format(format, this.round, this.remainingSelections.toString(),
                    allResults.toString(), winner, lowest);
        }

        public RoundResults(int round, List<Selection> remainingSelections) {
            this.round = round;
            this.remainingSelections = remainingSelections;
            allResults = new HashMap<>();
            for(Selection s : remainingSelections){ allResults.put(s, new ResultCount(s, 0));}
            Collections.sort(remainingSelections);
        }

        public void addVote(Selection choice) {
            if(!remainingSelections.contains(choice)){ return; }
            ResultCount choiceData = allResults.get(choice);
            if(choiceData == null){ choiceData = new ResultCount(choice, 0); }
            choiceData.addVote();
            allResults.put(choice, choiceData);
            totalVotes++;
        }

        public void evaluateRound() {
            BigDecimal totalVotesBD = new BigDecimal(totalVotes);
            ResultCount highest = null;
            for(Selection s : remainingSelections){
                ResultCount rc = allResults.get(s);
                BigDecimal countBD = new BigDecimal(rc.getCount());
                BigDecimal percent = countBD.divide(totalVotesBD,4, RoundingMode.HALF_UP);
                rc.setPercentage(percent);
                if(highest == null){ highest = rc; }
                else if(rc.getCount() > highest.getCount()){ highest = rc; }
                if(lowest == null){ lowest = rc; }
                else if(rc.getCount() < lowest.getCount()){ lowest = rc; }
            }

            if(highest.getPercentage().compareTo(new BigDecimal(0.5)) == 1) {
                winner = highest;
            }
        }

        public class ResultCount implements Comparable<ResultCount>{
            private Selection selection;
            private int count;
            private BigDecimal percentage;

            public ResultCount(Selection selection, int count){ this.selection = selection; this.count = count;}

            public void addVote(){ this.count++; }

            public Selection getSelection(){ return selection; }
            public void setSelection(Selection selection){ this.selection = selection;}

            public int getCount(){ return count;}
            public void setCount(int count){this.count = count;}

            public BigDecimal getPercentage(){ return percentage;}
            public void setPercentage(BigDecimal percentage){this.percentage = percentage;}

            public String toString() {
                String format = "Selection %s has %d votes, for %s percent of the vote";
                return String.format(format, selection.getName(), count, percentage.toString());
            }
            public int compareTo(ResultCount rc) {
                return rc.getCount() - this.getCount();
            }
        }
    }
}
