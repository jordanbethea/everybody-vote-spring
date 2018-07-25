package vote.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

/**
 * Method of ranked voting
 * Each round, checks if there is a candidate with a majority of all first place votes, if so, that is the winner
 * If not, eliminates the candidate with the most last place votes
 */

public class CoombsResults {
    Slate votedSlate;
    Selection overallWinner = null;
    int totalVotes;
    List<RoundResults> roundResults;

    public CoombsResults(List<Ballot> ballots) {
        List<List<RankedChoice>> allRankedChoices = new ArrayList<>();
        if(ballots.size() > 0){ votedSlate = ballots.get(0).getVotedSlate();}
        for(Ballot b : ballots) {
            List<RankedChoice> rankedChoices = new ArrayList<>();
            rankedChoices.addAll(b.getRankedChoices());
            allRankedChoices.add(rankedChoices);
        }
        List<Selection> remainingSelections = new ArrayList<>();
        remainingSelections.addAll(votedSlate.getSelections());

        boolean winner = false;
        int round = 0;
        while(!winner) {
            round++;
            RoundResults currentRound = new RoundResults(round,remainingSelections,allRankedChoices);
            roundResults.add(currentRound);
            if(currentRound.getWinner() != null) {
                winner = true;
                overallWinner = currentRound.getWinner().getSelection();
            }else {
                Selection lastPlaceSelection = currentRound.getLoser().getSelection();
                remainingSelections.remove(lastPlaceSelection);
                for(List<RankedChoice> choices : allRankedChoices) {
                    RankedChoice toRemove = null;
                    for(RankedChoice rc : choices) {
                        if(rc.getSelection().equals(lastPlaceSelection)){ toRemove = rc; break;}
                    }
                    choices.remove(toRemove);
                }
            }
        }
    }


    public class RoundResults {
        int round;
        int totalVotes;
        List<Selection> remainingSelections;
        List<ResultCount> firstPlaceVotes = new ArrayList<>();
        List<ResultCount> lastPlaceVotes = new ArrayList<>();
        ResultCount winner = null;
        ResultCount loser = null;

        public ResultCount getWinner(){ return winner; }
        public ResultCount getLoser(){ return loser;}

        public RoundResults(int round, List<Selection> remainingSelections, List<List<RankedChoice>> choices) {
            this.round = round;
            totalVotes = choices.size();
            this.remainingSelections = new ArrayList<>();
            this.remainingSelections.addAll(remainingSelections);
            Map<Selection, ResultCount> firstPlaceVotesMap = new HashMap<>();
            Map<Selection, ResultCount> lastPlaceVotesMap = new HashMap<>();
            for(Selection s : remainingSelections) {
                firstPlaceVotesMap.put(s, new ResultCount(s, 0));
                firstPlaceVotesMap.put(s, new ResultCount(s, 0));
            }

            for(List<RankedChoice> rankedSet : choices) {
                Collections.sort(rankedSet);
                Selection highestRank = rankedSet.get(0).getSelection();
                firstPlaceVotesMap.get(highestRank).addVote();

                Selection lowestRank = rankedSet.get(rankedSet.size() -1).getSelection();
                lastPlaceVotesMap.get(lowestRank).addVote();
            }
            BigDecimal totalVotesBD = new BigDecimal(totalVotes);
            firstPlaceVotes.addAll(firstPlaceVotesMap.values());
            Collections.sort(firstPlaceVotes);
            for(ResultCount rc : firstPlaceVotes){ rc.calcPercent(totalVotesBD);}
            lastPlaceVotes.addAll(lastPlaceVotesMap.values());
            Collections.sort(lastPlaceVotes);
            for(ResultCount rc : lastPlaceVotes){ rc.calcPercent(totalVotesBD);}

            loser = lastPlaceVotes.get(0);
            if(lastPlaceVotes.get(0).getPercentage().compareTo(new BigDecimal(0.5)) == 1){
                winner = lastPlaceVotes.get(0);
            }
        }


        public class ResultCount implements Comparable<ResultCount>{
            private Selection selection;
            private int count;
            private BigDecimal percentage;

            public ResultCount(Selection selection, int count){ this.selection = selection; this.count = count;}

            public void addVote(){ this.count++; }
            public void calcPercent(BigDecimal totalVotes) {
                BigDecimal countBD = new BigDecimal(count);
                BigDecimal percent = countBD.divide(totalVotes,4, RoundingMode.HALF_UP);
                setPercentage(percent);
            }

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
