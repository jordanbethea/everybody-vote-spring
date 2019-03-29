package vote.model.dataTransferObjects;
import lombok.Data;

@Data
public class RankedChoiceDTO {
    SelectionDTO sel;
    BallotDTO ballot;
    int ranking;
}
