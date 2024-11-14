package pl.akademiaqa.dto.requests;

import lombok.Data;

@Data
public class CreateTaskRequestDto {

    private String name;
    private String description;
    private String assignees;
    private String status;
    private String priority;

}
