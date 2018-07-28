package vote.model;

import javax.persistence.*;

@Entity
public class RankedChoice implements Comparable<RankedChoice>{

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch=FetchType.EAGER)
    Selection sel;

    @ManyToOne(fetch=FetchType.EAGER)
    Ballot ballot;

    int ranking;

    public RankedChoice(){}

    public RankedChoice(Selection sel, int ranking) {
        this.sel = sel;
        this.ranking = ranking;
    }

    public RankedChoice(Selection sel, int ranking, Ballot ballot) {
        this.sel = sel;
        this.ranking = ranking;
        this.ballot = ballot;
    }

    public Long getID(){ return this.id; }
    public void setID(Long id){ this.id = id;}

    public Selection getSelection(){ return sel; }
    public void setSelection(Selection sel){this.sel = sel;}

    public int getRanking(){ return ranking;}
    public void setRanking(int ranking){this.ranking = ranking;}

    public Ballot getBallot(){ return ballot;}
    public void setBallot(Ballot ballot){this.ballot = ballot;}

    @Override
    public int compareTo(RankedChoice other) {
        return this.ranking - other.ranking;
    }

    @Override
    public String toString() {
        String format = "Ranked Choice[id: %s, Selection: %s (Position: %d), Ranking %d]";
        return String.format(format, this.getID(), this.getSelection().getName(),
                this.getSelection().getPosition(), this.getRanking());
    }
}
