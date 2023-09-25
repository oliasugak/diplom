package adaptiveschool.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class TeacherNamesDTO {

    private Integer id;

    private String firstname;

    private String lastname;

    private String patronymic;

    public Integer getId() {
        return id;
    }
}
