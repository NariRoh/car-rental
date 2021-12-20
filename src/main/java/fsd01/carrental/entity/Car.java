package fsd01.carrental.entity;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "cars")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String make;

    private String model;

    private int seats;

    private int mileage;

    private String transmission;

    private String fuelType;

    private double hourlyRate;

    private double dailyRate;

    private double monthlyLeasingRate;

    private int noBookings;

    @Lob
    @Type(type="org.hibernate.type.BinaryType")
    private byte[] imgData;

    private String imgType;

    private String imgName;

    @Transient
    private int rating;

    @Transient
    private String imgString;
}
