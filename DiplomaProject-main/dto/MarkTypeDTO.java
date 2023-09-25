package adaptiveschool.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarkTypeDTO {

    int id;

    private String markType;

    private String description;

    private boolean isActive;
}
