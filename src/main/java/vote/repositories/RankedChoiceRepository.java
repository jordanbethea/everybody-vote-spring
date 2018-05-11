package vote.repositories;

import org.springframework.data.repository.CrudRepository;
import vote.model.RankedChoice;

public interface RankedChoiceRepository extends CrudRepository<RankedChoice, Long>{
}
