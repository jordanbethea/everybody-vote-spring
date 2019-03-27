package vote.repositories;

import org.springframework.data.repository.CrudRepository;
import vote.model.entities.Ballot;
import java.util.List;
import vote.model.entities.Slate;

public interface BallotRepository extends CrudRepository<Ballot, Long>{
    List<Ballot> findByVotedSlate(Slate votedSlate);
}
