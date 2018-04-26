package vote.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.FetchType;

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

    public Ballot(){}

    public Ballot(String voter, Slate votedSlate) {
        this.voter = voter;
        this.votedSlate = votedSlate;
    }

    public Long getId(){ return id; }
    public void setId(Long id){ this.id = id; }

    public String getVoter(){ return voter;}
    public void setVoter(String voter){this.voter = voter; }

    public Slate getVotedSlate(){ return votedSlate; }
    public void setVotedSlate(Slate votedSlate){this.votedSlate = votedSlate;}

    public Selection getSingleVoteChoice(){ return singleVoteChoice;}
    public void setSingleVoteChoice(Selection singleVoteChoice){this.singleVoteChoice = singleVoteChoice;}

}
