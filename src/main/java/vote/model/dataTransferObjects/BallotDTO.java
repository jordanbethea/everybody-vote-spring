package vote.model.dataTransferObjects;
import lombok.Data;
import java.util.List;

@Data
public class BallotDTO {
    private String voter;
    private SlateDTO votedSlate;
    private SelectionDTO singleVoteChoice;
    List<RankedChoiceDTO> rankedChoices;

}
