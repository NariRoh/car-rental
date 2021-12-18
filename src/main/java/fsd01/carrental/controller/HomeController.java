package fsd01.carrental.controller;

import fsd01.carrental.entity.Car;
import fsd01.carrental.repository.CarRepository;
import fsd01.carrental.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Base64;
import java.util.HashMap;

@Controller
public class HomeController {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @GetMapping({"/"})
    public ModelAndView showIndex() {
        ModelAndView mav = new ModelAndView("index");
        int total = 0;
        int counter = 0;
        HashMap<Car, String> featuredCarsWithImage = new HashMap();
        int carCounter = 0;
        for (Car car : carRepository.getIndexFeaturedCars()) {
            if (carCounter < 4) {
                for (Integer rating : reviewRepository.getReviewsOfCar(car.getId())) {
                    total += rating;
                    counter ++;
                }
                if (counter == 0) {
                    car.setRating(0);
                } else {
                    car.setRating((int)Math.ceil(total/counter));
                }
                total = 0;
                counter = 0;
                featuredCarsWithImage.put(car, Base64.getEncoder().encodeToString(car.getImgData()));
                carCounter++;
            } else {
                break;
            }
        }
        mav.addObject("cars", featuredCarsWithImage);
        return mav;
    }
}
