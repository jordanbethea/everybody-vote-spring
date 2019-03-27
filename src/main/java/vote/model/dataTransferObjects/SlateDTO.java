package vote.model.dataTransferObjects;

import java.util.Date;

import lombok.Data;
import vote.model.dataTransferObjects.SelectionDTO;
import java.util.List;
import java.io.Serializable;


@Data
public class SlateDTO implements Serializable {
    private String creator;
    private String topic;
    private Date dateCreated;

    private List<SelectionDTO> selections;
}
