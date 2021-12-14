package fsd01.carrental.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PasswordDTO {

    private Long id;

    @Length(min = 6, message = "Password must be at least 6 characters long")
    @NotEmpty(message = "Please enter your current password")
    private String password;

    @NotEmpty(message = "Please enter new password")
    @Pattern(regexp = "^(?=.*?[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[^\\w\\d]).*$",
            message = "Password must contain one uppercase, one lowercase, one number, and one special character")
    private String newPassword;
}
