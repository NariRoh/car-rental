package fsd01.carrental.controller;

import fsd01.carrental.entity.Booking;
import fsd01.carrental.entity.Car;
import fsd01.carrental.entity.User;
import fsd01.carrental.repository.BookingRepository;
import fsd01.carrental.repository.CarRepository;
import fsd01.carrental.service.StripeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @Value("${STRIPE_SECRET_KEY}")
    private String API_PUBLIC_KEY;

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
        booking.setUser(user);
        booking.setCar(bookingCar);
        System.out.println(booking.getStripeToken());

        if (bindingResult.hasErrors()) {
            return "redirect:/404";
        }

        if (booking.getPricingOption().equals("Hourly")) {
            booking.setDropOffDate(booking.getPickupDate());
        }

        bookingRepository.save(booking);
        bookingCar = null;
        return "index";
    }

//    @GetMapping("/charge")
//    public String chargePage(Model model) {
//        model.addAttribute("stripePublicKey", API_PUBLIC_KEY);
//        return "charge";
//    }
//
//    @PostMapping("/create-charge")
//    public @ResponseBody Response createCharge(String email, String token) {
//        //validate data
//        if (token == null) {
//            return new Response(false, "Stripe payment token is missing. Please, try again later.");
//        }
//
//        //create charge
//        String chargeId = stripeService.createCharge(email, token, 999); //$9.99 USD
//        if (chargeId == null) {
//            return new Response(false, "An error occurred while trying to create a charge.");
//        }
//
//        // You may want to store charge id along with order information
//
//        return new Response(true, "Success! Your charge id is " + chargeId);
//    }
}
