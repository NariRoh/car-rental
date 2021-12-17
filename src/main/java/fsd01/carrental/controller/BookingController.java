package fsd01.carrental.controller;

import fsd01.carrental.entity.Booking;
import fsd01.carrental.entity.Car;
import fsd01.carrental.entity.User;
import fsd01.carrental.repository.BookingRepository;
import fsd01.carrental.repository.CarRepository;
import fsd01.carrental.service.StripeService;
import fsd01.carrental.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Base64;
import java.util.HashMap;

@Controller
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private StripeService stripeService;

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
    public String handleBooking(@ModelAttribute Booking booking, User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "redirect:/404";
        }

        if (booking.getPricingOption().equals("Hourly")) {
            booking.setDropOffDate(booking.getPickupDate());
        }

        stripeService.createCharge(booking);
        booking.setUser(user);
        booking.setCar(bookingCar);
        bookingRepository.save(booking);
        bookingCar = null;

        return "booking";
    }

}
