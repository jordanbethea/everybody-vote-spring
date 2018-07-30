package vote.model;

import jdk.internal.dynalink.linker.ConversionComparator;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

/**
 * Condorcet method - does head to head comparisons of every pair of Selections
 * final rank is by number of victories in comparisons
 */
public class CopelandResults {
    Slate votedSlate;
    Selection winner;
    List<ComparisonResults> allResults = new ArrayList<>();
    List<SelectionResults> selectionResults = new ArrayList<>();

    public Slate getVotedSlate(){ return votedSlate;}
    public Selection getWinner(){ return winner; }
    public List<ComparisonResults> getComparisonResults(){ return allResults;}
    public List<SelectionResults> getSelectionResults(){ return selectionResults;}

    @Override
    public String toString() {
        String format = "Copeland Results[Slate: %s %nComparison Results: %s %nSelection Results: %s]";
        return String.format(format, votedSlate.getTopic(), allResults, selectionResults);
    }

    //right now is looping through each combination, and then looping through all ballots to find that combination in the ranks
    //can probably make a faster way by looping through ballots, and then inside ballot, pulling each combination?
    public CopelandResults(List<Ballot> ballots) {
        if(ballots.size() > 0){ votedSlate = ballots.get(0).getVotedSlate(); }
        else{ return; }
        Map<String, Boolean> existingCombinations = new HashMap<>();

        //Run comparisons for each pair of selections.
        int numSelections = votedSlate.getSelections().size();
        for(int i = 0;i<numSelections - 1;i++) {
            Selection s1 = votedSlate.getSelections().get(i);
            for(int j = i+1;j<numSelections;j++) {
                Selection s2 = votedSlate.getSelections().get(j);
                ComparisonResults results = new ComparisonResults(s1,s2,ballots);
                allResults.add(results);
            }
        }
/*        for(Selection s1 : votedSlate.getSelections()) {
            for(Selection s2 : votedSlate.getSelections()) {
                String comboName = s1.getName() + s2.getName();
                if(existingCombinations.get(comboName) != null){ break; }
                ComparisonResults results = new ComparisonResults(s1,s2,ballots);
                allResults.add(results);
                existingCombinations.put(comboName, true);
            }
        } */
        Map<Selection, SelectionResults> selResultsMap = new HashMap<>();
        for(ComparisonResults cr : allResults) {
            SelectionResults sr1 = selResultsMap.get(cr.getComp1());
            if(sr1 == null){ sr1 = new SelectionResults(cr.getComp1());}

            SelectionResults sr2 = selResultsMap.get(cr.getComp2());
            if(sr2 == null){ sr2 = new SelectionResults(cr.getComp2());}

            if(cr.getComp1Count() == cr.getComp2Count()) {
                sr1.addTie();
                sr2.addTie();
            }
            else if(cr.getComp1Count() > cr.getComp2Count()) {
                sr1.addWin();
                sr2.addLoss();
            }else { sr1.addLoss(); sr2.addWin();}

            selResultsMap.put(cr.getComp1(), sr1);
            selResultsMap.put(cr.getComp2(), sr2);
        }
        selectionResults.addAll(selResultsMap.values());
        Collections.sort(selectionResults);
        winner = selectionResults.get(0).getSelection();
    }

    public class SelectionResults implements Comparable<SelectionResults>{
        private Selection selection;
        private int wins;
        private int losses;
        private int ties;
        private int net;

        public Selection getSelection(){ return selection;}
        public int getWins(){ return wins;}
        public int getLosses(){ return losses;}
        public int getTies(){ return ties; }
        public int getNet(){ return net;}
        public void addWin(){ this.wins++; setNet();}
        public void addLoss(){ this.losses++; setNet();}
        public void addTie(){ this.ties++; }
        public void setNet(){ this.net = this.wins - this.losses;}

        public SelectionResults(Selection s) {
            this.selection = s;
            this.wins = 0;
            this.losses = 0;
            this.ties = 0;
            this.net = 0;
        }

        @Override
        public String toString() {
            String format = "Selection Results for Selection: %s:[Wins: %d, Losses: %d, Ties: %d, Net total: %d %n]";
            return String.format(format, selection.getName(), wins, losses, ties, net);
        }

        @Override
        public int compareTo(SelectionResults sr) {
            return sr.net - this.net;
        }
    }

    public class ComparisonResults {
        private Selection comp1;
        private Selection comp2;
        private int comp1Count;
        private int comp2Count;

        public ComparisonResults(Selection comp1, Selection comp2, List<Ballot> ballots) {
            this.comp1 = comp1;
            this.comp2 = comp2;
            for(Ballot vote : ballots) {
                for(RankedChoice rc : vote.getRankedChoices()) {
                    if(rc.getSelection().equals(comp1)) {
                        comp1Count++;
                        break;
                    }else if(rc.getSelection().equals(comp2)) {
                        comp2Count++;
                        break;
                    }
                }
            }
        }

        @Override
        public String toString() {
            String format = "Comparison Results[Selection 1: %s, wins count: %d.  Selection 2: %s, wins count: %d %n]";
            return String.format(format, comp1.getName(), comp1Count, comp2.getName(), comp2Count);
        }

        public Selection getComp1(){ return comp1;}
        public Selection getComp2(){ return comp2;}
        public int getComp1Count(){ return comp1Count;}
        public int getComp2Count(){ return comp2Count;}
    }
}
