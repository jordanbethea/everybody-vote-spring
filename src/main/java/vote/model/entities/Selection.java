package vote.model.entities;

import javax.persistence.*;

/**
 * Represents an item that can be chosen while voting.
 * Right now just name and description, but making it an object for ease of lookups
 * and in case want to add more data later.
 */

//TODO - see if I want to make this embeddable, don't know much about it yet
@Entity
public class Selection implements Comparable<Selection>{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String name;
    private String description;
    private int position; //position on the slate ordering (if position is manual)

    @ManyToOne(fetch=FetchType.EAGER)
    private Slate slate;

    public Selection(){}

    public Selection(String name, String description, int position, Slate slate){
        this.name = name;
        this.description = description;
        this.position = position;
        this.slate = slate;
    }

    public Long getId(){ return id; }
    public void setId(Long id){ this.id = id; }

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
        return String.format("Selection[id=%1$s, name=%2$s, description=%3$s, position=%4$s]",
                this.id, this.name,this.description,this.position);
    }

    @Override
    public int compareTo(Selection other) {
        return this.position - other.position;
    }
}
