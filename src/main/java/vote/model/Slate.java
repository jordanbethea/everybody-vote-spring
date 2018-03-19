package vote.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.OneToMany;

import java.util.List;
import java.util.Date;

/**
 * The generic set of items to be voted on. This is what a person initially creates
 * when they want to call a vote on something.
 */
@Entity
public class Slate {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    //name of person who created slate, no account system so no user id
    private String creator;
    private Date dateCreated;

    @OneToMany(mappedBy="slate")
    private List<Selection> selections;

    //generic constructor required by jpa
    protected Slate(){}

    public Slate(List<Selection> selections, String creator, Date dateCreated){
        this.selections = selections;
        this.creator = creator;
        this.dateCreated = dateCreated;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        String slateDesc = String.format("Slate[creator=%s, dateCreated=%d", this.creator, this.dateCreated);
        sb.append(slateDesc);
        for(Selection s : selections){
            sb.append(s.toString());
        }
        return sb.toString();
    }

}
