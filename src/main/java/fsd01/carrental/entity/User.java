package fsd01.carrental.entity;

import fsd01.carrental.security.ApplicationUserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Collection;

@Entity
@Table(name = "users")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Please enter your first name")
    @Column(length = 50)
    private String firstName;

    @NotBlank(message = "Please enter your last name")
    @Column(length = 50)
    private String lastName;
    
    @Email(message = "Please enter a valid email")
    @NotBlank(message = "Please enter your email")
    @Column(length = 150, unique = true)
    private String email;

    @NotBlank(message = "Please enter a password")
    @Pattern(regexp =
                    "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{6,100}$",
            message =
                    "Password must be 6 - 100 characters long including one uppercase, " +
                            "one lowercase, one number, and one special character.")
    private String password;

    @Range(min = 10,max= 10, message = "Phone number should be exact 10 characters" )
    private String phoneNumber;

    @Enumerated
    private ApplicationUserRole role;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
