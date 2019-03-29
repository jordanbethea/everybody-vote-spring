package vote.model.dataTransferObjects;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import vote.model.rankingCalculators.*;

@Data
public class ResultsDTO {
    private SlateDTO slate;
    List<BallotDTO> ballots;
    Map<SelectionDTO, Integer> firstPastPostResults = new HashMap<>();



    private void generateFPTPResults(){
        for(BallotDTO b : ballots) {
            SelectionDTO s = b.getSingleVoteChoice();
            Integer count = firstPastPostResults.get(s);
            if(count == null){ count = 0;}
            count++;
            firstPastPostResults.put(s, count);
        }
    }
}
