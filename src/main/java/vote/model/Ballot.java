package vote.model;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
public class Ballot {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    String voter;

    @OneToOne(fetch=FetchType.EAGER)
    Slate votedSlate;

    @OneToOne(fetch=FetchType.EAGER)
    Selection singleVoteChoice;

    @OneToMany(mappedBy="ballot",fetch=FetchType.EAGER)
    @OrderBy("ranking")
    List<RankedChoice> rankedChoices;

    public Ballot(){rankedChoices = new ArrayList<RankedChoice>();}

    public Ballot(String voter, Slate votedSlate) {
        rankedChoices = new ArrayList<RankedChoice>();
        this.voter = voter;
        this.votedSlate = votedSlate;
        createRankedChoicesFromSlate();
    }

    private void createRankedChoicesFromSlate() {
        for(Selection s: votedSlate.getSelections()) {
            RankedChoice newChoice = new RankedChoice(s, 0);
            rankedChoices.add(newChoice);
        }
    }

    public Ballot(String voter, Slate votedSlate, List<RankedChoice> rankedChoices) {
        this.voter = voter;
        this.votedSlate = votedSlate;
        this.rankedChoices = rankedChoices;
    }

    public Long getId(){ return id; }
    public void setId(Long id){ this.id = id; }

    public String getVoter(){ return voter;}
    public void setVoter(String voter){this.voter = voter; }

    public Slate getVotedSlate(){ return votedSlate; }
    public void setVotedSlate(Slate votedSlate){
        this.votedSlate = votedSlate;
        if(this.rankedChoices.size() == 0) {
            createRankedChoicesFromSlate();
        }
    }

    public Selection getSingleVoteChoice(){ return singleVoteChoice;}
    public void setSingleVoteChoice(Selection singleVoteChoice){this.singleVoteChoice = singleVoteChoice;}

    public List<RankedChoice> getRankedChoices(){return rankedChoices;}
    public void setRankedChoices(List<RankedChoice> rankedChoices){this.rankedChoices = rankedChoices;}

    public void rankVoteUp(int index) {
        if(index == 0){ return; }
        RankedChoice selected = rankedChoices.remove(index);
        rankedChoices.add(index - 1, selected);
    }

    public void rankVoteDown(int index) {
        if(index >= rankedChoices.size()-1){ return; }
        RankedChoice selected = rankedChoices.remove(index);
        int newIndex = index + 1;
        if(newIndex >= rankedChoices.size()){rankedChoices.add(selected);}
        else{ rankedChoices.add(newIndex, selected);}
    }
}
