package vote.model.rankingCalculators;

import vote.model.entities.Slate;
import vote.model.entities.Selection;
import vote.model.entities.Ballot;
import vote.model.entities.RankedChoice;
import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class BordaCount {
    public enum COUNT_TYPE {
        BASE_0 {
            @Override
            public BigDecimal getCountValue(int total, int rank) {
                if(total <= 0 || rank <= 0){ return BigDecimal.ZERO; }
                return new BigDecimal(total - rank);
            }
        }, BASE_1 {
            @Override
            public BigDecimal getCountValue(int total, int rank) {
                if(total <= 0 || rank <= 0){ return BigDecimal.ZERO;}
                return new BigDecimal(total - rank +1);
            }
        }, DOWDALL {
            @Override
            public BigDecimal getCountValue(int total, int rank) {
                if(rank <= 0){ return BigDecimal.ZERO; }
                return new BigDecimal(1).divide(new BigDecimal(rank),6, BigDecimal.ROUND_HALF_EVEN);
            }
        };

        public abstract BigDecimal getCountValue(int total, int rank);
    }

    Slate votedSlate = null;
    COUNT_TYPE countType;
    List<SelectionResults> selectionResults = new ArrayList<>();
    Selection winningSelection;

    public Selection getWinningSelection(){ return winningSelection;}
    public List<SelectionResults> getSelectionResults(){ return selectionResults;}

    public BordaCount(COUNT_TYPE countType, List<Ballot> ballots) {
        if(ballots.size() > 0){ this.votedSlate = ballots.get(0).getVotedSlate();}
        if(this.votedSlate == null){ return; }
        this.countType = countType;
        int selectionCount = votedSlate.getSelections().size();

        Map<Selection, SelectionResults> selResultsMap = new HashMap<>();
        for(Selection s : votedSlate.getSelections()) {
            selResultsMap.put(s, new SelectionResults(s, selectionCount));
        }

        for(Ballot ballot : ballots) {
            for(RankedChoice rc : ballot.getRankedChoices()) {
                selResultsMap.get(rc.getSelection()).addVote(rc.getRanking());
            }
        }
        for(SelectionResults sr : selResultsMap.values()) {
            sr.calcTotalScore(this.countType);
            selectionResults.add(sr);
        }
        Collections.sort(selectionResults);
        winningSelection = selectionResults.get(0).getSelection();
    }

    @Override
    public String toString() {
        String format = "Borda Count. Slate voted on: %s.%n Count Type: %s. %nAll selection Results: %s%n. Winner: %s";
        return String.format(format, votedSlate.getTopic(), countType.toString(), selectionResults.toString(), winningSelection.getName());
    }

    public class SelectionResults implements Comparable<SelectionResults>{
        Selection sel;
        Map<Integer, Integer> rankCounts = new HashMap<>();
        BigDecimal totalScore = BigDecimal.ZERO;
        int totalSelections;

        public SelectionResults(Selection sel, int totalSelections) {
            this.sel = sel;
            this.totalSelections = totalSelections;
        }

        public Selection getSelection(){ return sel; }
        public Map<Integer, Integer> getRankCounts(){ return rankCounts;}
        public BigDecimal getTotalScore(){ return totalScore;}
        public void addVote(int rank) {
            Integer count = rankCounts.get(rank);
            if (count == null) { count = 0; }
            count++;
            rankCounts.put(rank, count);
        }

        public void calcTotalScore(COUNT_TYPE countType) {
            Set<Integer> ranks = rankCounts.keySet();
            for(Integer rank : ranks) {
                int count = rankCounts.get(rank);
                BigDecimal rate = countType.getCountValue(totalSelections, rank);
                BigDecimal total = rate.multiply(new BigDecimal(count));
                totalScore = totalScore.add(total);
            }
        }

        @Override
        public String toString() {
            String format = "Results for selection: %s. All Rank Counts: %n%s %nTotal Score: %s%n";
            return String.format(format, sel.toString(), rankCounts.toString(), totalScore.toEngineeringString());
        }

        @Override
        public int compareTo(SelectionResults sr) {
            return sr.getTotalScore().compareTo(this.getTotalScore());
        }
    }
}
