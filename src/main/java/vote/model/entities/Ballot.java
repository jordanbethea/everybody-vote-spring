package vote.model.entities;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
public class Ballot {
    private static final Logger log = LoggerFactory.getLogger(Ballot.class);


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
            RankedChoice newChoice = new RankedChoice(s, 0, this);
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

    /**
     * Puts the ranked voting selections in the specified order based on selection ordering
     * used mainly for unit testing
     * @param order
     */
    public void rankingOrder(List<Integer> order) {
        int rankingPos = 1;
        if(order.size() != votedSlate.getSelections().size()){ return; }
        for(Integer i = 1;i <= order.size();i++){ if(!order.contains(i)){return;}}
        //log.info("Input order: "+order.toString());
        //first, ensure list is same size as list of selections, and contains 1..x values
        List<RankedChoice> choices = new ArrayList<>();
        for(Integer choice : order) {
            for(Selection s: votedSlate.getSelections()) {
                //log.info("position: "+s.getPosition());
                if(choice.equals(s.getPosition())){
                    RankedChoice ranking = new RankedChoice(s,rankingPos,this);
                    choices.add(ranking);
                    rankingPos++;
                    break;
                }
            }
        }
        if(rankedChoices.size() != choices.size()){
            log.info("New list of ranked choices not same size as existing: "+choices.size());
            log.info(rankedChoices.toString());
            log.info(choices.toString());
        }
        rankedChoices = choices;
    }
}
