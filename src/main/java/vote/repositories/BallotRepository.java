package vote.repositories;

import org.springframework.data.repository.CrudRepository;
import vote.model.Ballot;
import java.util.List;
import vote.model.Slate;

public interface BallotRepository extends CrudRepository<Ballot, Long>{
    List<Ballot> findByVotedSlate(Slate votedSlate);
}
