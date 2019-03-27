package vote.repositories;

import org.springframework.data.repository.CrudRepository;
import vote.model.entities.RankedChoice;

public interface RankedChoiceRepository extends CrudRepository<RankedChoice, Long>{
}
