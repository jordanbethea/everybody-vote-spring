package vote.model.dataTransferObjects;

import lombok.Data;

import java.io.Serializable;

@Data
public class SelectionDTO implements Serializable {
    private String name;
    private String description;
    private int position;
}
