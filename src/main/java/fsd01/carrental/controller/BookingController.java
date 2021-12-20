package fsd01.carrental.controller;

import fsd01.carrental.entity.Booking;
import fsd01.carrental.entity.Car;
import fsd01.carrental.entity.User;
import fsd01.carrental.model.Mail;
import fsd01.carrental.repository.BookingRepository;
import fsd01.carrental.repository.CarRepository;
import fsd01.carrental.service.EmailSenderService;
import fsd01.carrental.service.StripeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Controller
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private StripeService stripeService;

    @Autowired
    private EmailSenderService emailService;

    private Car bookingCar;

    @GetMapping("/booking/{id}")
    public ModelAndView showCar(@PathVariable long id) {
        ModelAndView mav = new ModelAndView("booking");
        HashMap<Car, String> carWithImage = new HashMap<Car, String>();
        carWithImage.put(carRepository.findCarById(id), Base64.getEncoder().encodeToString(carRepository.findCarById(id).getImgData()));
        mav.addObject("cars", carWithImage);
        bookingCar = carRepository.findCarById(id);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        mav.addObject("user", user);

        Booking booking = new Booking();
        mav.addObject("booking", booking);

        return mav;
    }

    @PostMapping("/booking")
    public String handleBooking(@ModelAttribute Booking booking, User user, BindingResult bindingResult) throws MessagingException, IOException {

        if (bindingResult.hasErrors()) {
            return "redirect:/404";
        }

        if (booking.getPricingOption().equals("Hourly")) {
            booking.setDropOffDate(booking.getPickupDate());
        }

        stripeService.createCharge(booking);
        booking.setUser(user);
        booking.setCar(bookingCar);
//        booking.setTotal(Math.round(booking.getTotal()));
        bookingRepository.save(booking);

        Mail mail = new Mail();
        mail.setFrom("RealEstateProjectNFC@gmail.com");
        mail.setMailTo(booking.getRenterEmail());
        mail.setSubject("Car Rental - Order Confirmation");
        Map<String, Object> emailData = new HashMap<String, Object>();
        emailData.put("firstName", booking.getRenterFirstName());
        emailData.put("lastName", booking.getRenterLastName());
        emailData.put("total", booking.getTotal());
        emailData.put("make", bookingCar.getMake());
        emailData.put("model", bookingCar.getModel());
        emailData.put("pickupDate", booking.getPickupDate());
        emailData.put("dropOffDate", booking.getDropOffDate());
        mail.setProps(emailData);
        emailService.sendEmail(mail);

        bookingCar = null;

        return "booking";
    }

}
