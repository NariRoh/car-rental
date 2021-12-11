package fsd01.carrental.controller;

import fsd01.carrental.entity.Car;
import fsd01.carrental.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

@Controller
public class CarController {

    @Autowired
    private CarRepository carRepository;

    @GetMapping({"/cars"})
    public ModelAndView showCarList() {
        ModelAndView mav = new ModelAndView("cars");
        HashMap<Car, String> carsWithImage = new HashMap<Car, String>();
        for (Car car:carRepository.getListOfCars()) {
            carsWithImage.put(car, Base64.getEncoder().encodeToString(car.getImgData()));
        }
        mav.addObject("cars", carsWithImage);
        return mav;
    }

    @GetMapping("/car/{id}")
    public ModelAndView showCar(@PathVariable long id) {
        ModelAndView mav = new ModelAndView("car-single");
        mav.addObject("car", carRepository.findCarById(id));
        return mav;
    }

}
