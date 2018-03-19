package vote.model;

import javax.persistence.*;

/**
 * Represents an item that can be chosen while voting.
 * Right now just name and description, but making it an object for ease of lookups
 * and in case want to add more data later.
 */

//TODO - see if I want to make this embeddable, don't know much about it yet
@Entity
public class Selection {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String name;
    private String description;
    private int position; //position on the slate ordering (if position is manual)

    @ManyToOne
    private Slate slate;

    protected Selection(){}

    public Selection(String name, String description, int position, Slate slate){
        this.name = name;
        this.description = description;
        this.position = position;
        this.slate = slate;
    }

    public String getName(){ return name; }
    public void setName(String name){ this.name = name;}

    public String getDescription(){ return description;}
    public void setDescription(String description){this.description = description;}

    public int getPosition(){ return position; }
    public void setPosition(int position){this.position = position;}

    public Slate getSlate(){ return slate;}
    public void setSlate(Slate slate){this.slate = slate;}

    @Override
    public String toString(){
        return String.format("Selection[id=%d, name=%s, description=%s, position=%i",
                this.id, this.name,this.description,this.position);
    }
}
