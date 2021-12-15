package fsd01.carrental.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    private Car car;

    private String pricingOption;

    private LocalDateTime pickupDate;

    private LocalDateTime dropOffDate;

    private LocalDateTime pickupTime;

    private LocalDateTime dropOffTime;

    private double total;

    @Pattern(regexp = "^[a-z][a-z '-.,]{1,30}$", message = "First name must be between 1 - 30 characters and must not contain special characters.")
    private String renterFirstName;

    @Pattern(regexp = "^[a-z][a-z '-.,]{1,30}$", message = "Last name must be between 1 - 30 characters and must not contain special characters.")
    private String renterLastName;

    @Email(message = "Please provide a valid email address.")
    @Size(min = 3, max = 320, message = "Email must be between 3 - 320 characters.")
    private String renterEmail;

}
