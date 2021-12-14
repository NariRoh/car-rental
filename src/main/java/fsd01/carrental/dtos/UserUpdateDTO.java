package fsd01.carrental.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserUpdateDTO {

    private Long id;

    @Size(min = 1, max = 50, message = "First name should be between 1 - 50 characters long")
    private String firstName;

    @Size(min = 1, max = 50, message = "Last name should be between 1 - 50 characters long")
    private String lastName;

    private String phoneNumber;
}
