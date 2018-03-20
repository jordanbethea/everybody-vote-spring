package vote.model;

import javax.persistence.*;

import java.util.List;
import java.util.ArrayList;
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
    //What you are voting on
    private String topic;
    private Date dateCreated;

    @OneToMany(mappedBy="slate", fetch= FetchType.EAGER)
    private List<Selection> selections = new ArrayList<Selection>();

    //generic constructor required by jpa
    protected Slate(){}

    public Slate(String topic, String creator, Date dateCreated){
        this.selections = new ArrayList<Selection>();
        this.topic = topic;
        this.creator = creator;
        this.dateCreated = dateCreated;
    }

    public void setCreator(String creator){ this.creator = creator;}
    public String getCreator(){return this.creator;}

    public void setDateCreated(Date dateCreated){this.dateCreated = dateCreated;}
    public Date getDateCreated(){ return this.dateCreated;}

    public void setTopic(String topic){this.topic = topic; }
    public String getTopic(){return this.topic;}

    public void setSelections(List<Selection> selections){ this.selections = selections;}
    public List<Selection> getSelections(){ return this.selections;}
    public void addSelection(Selection s){ this.selections.add(s);}

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        String slateDesc = String.format("Slate[topic=%1$s, creator=%2$s, dateCreated=%3$s",
                this.topic, this.creator, this.dateCreated);
        sb.append(slateDesc);
        for(Selection s : selections){
            sb.append(s.toString());
        }
        return sb.toString();
    }

}
