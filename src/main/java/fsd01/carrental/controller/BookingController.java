package fsd01.carrental.controller;

import fsd01.carrental.entity.Car;
import fsd01.carrental.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.Base64;
import java.util.HashMap;

@Controller
public class BookingController {

    @Autowired
    private CarRepository carRepository;

    @GetMapping("/booking/{id}")
    public ModelAndView showCar(@PathVariable long id) {
        ModelAndView mav = new ModelAndView("booking");
        HashMap<Car, String> carWithImage = new HashMap<Car, String>();
        carWithImage.put(carRepository.findCarById(id), Base64.getEncoder().encodeToString(carRepository.findCarById(id).getImgData()));
        mav.addObject("cars", carWithImage);
        return mav;
    }

}
