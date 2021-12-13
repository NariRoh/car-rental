package fsd01.carrental.dtos;

import fsd01.carrental.security.UserRole;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {

    private long id;

    // TODO: first & lastname size validation?

    @NotBlank(message = "Please enter your first name")
    private String firstName;

    @NotBlank(message = "Please enter your last name")
    private String lastName;

    @Email(message = "Please enter a valid email")
    @NotBlank(message = "Please enter your email")
    private String email;

    @Length(min = 6, message = "Password must be at least 6 characters long")
    @NotBlank(message = "Please enter your password")
    @Pattern(regexp = "^(?=.*?[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[^\\w\\d]).*$",
             message = "Password must contain one uppercase, one lowercase, one number, and one special character")
    private String password;

    private String phoneNumber;

    //    private UserRole role;

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
