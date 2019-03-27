package vote.repositories;

import vote.model.entities.Slate;
import org.springframework.data.repository.CrudRepository;

public interface SlateRepository extends CrudRepository<Slate, Long>{
}
