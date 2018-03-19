package vote.repositories;

import vote.model.Slate;
import org.springframework.data.repository.CrudRepository;

public interface SlateRepository extends CrudRepository<Slate, Long>{
}
