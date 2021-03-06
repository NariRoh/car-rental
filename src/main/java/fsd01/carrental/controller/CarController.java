package fsd01.carrental.controller;

import fsd01.carrental.entity.Car;
import fsd01.carrental.entity.Review;
import fsd01.carrental.repository.CarRepository;
import fsd01.carrental.repository.ReviewRepository;
import fsd01.carrental.service.CarService;
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

    @Autowired
    private CarService carService;

    @Autowired
    private ReviewRepository reviewRepository;

    @GetMapping({"/cars"})
    public ModelAndView showCarList(
            String priceRangeOptions,
            String transmissionOptions, String mileageOptions,
            String fuelTypeOptions, String seatsOptions
    ) {
        ModelAndView mav = new ModelAndView("cars");
        int total = 0;
        int counter = 0;
        HashMap<Car, String> carsWithImage = new HashMap<Car, String>();
        for (Car car :
                carRepository.getListOfCarsSearch(
                        priceRangeParam(priceRangeOptions)[0],
                        priceRangeParam(priceRangeOptions)[1],
                        transmissionParams(transmissionOptions),
                        mileageParams(mileageOptions)[0],
                        mileageParams(mileageOptions)[1],
                        fuelTypeParams(fuelTypeOptions),
                        seatsParams(seatsOptions)[0],
                        seatsParams(seatsOptions)[1])
        ) {
            for (Integer rating : reviewRepository.getRatingsOfCar(car.getId())) {
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
            carsWithImage.put(car, Base64.getEncoder().encodeToString(car.getImgData()));
        }
        mav.addObject("cars", carsWithImage);
        return mav;
    }

    @GetMapping("/car/{id}")
    public ModelAndView showCar(@PathVariable long id) {
        ModelAndView mav = new ModelAndView("car-single");
        HashMap<Car, String> carWithImage = new HashMap<Car, String>();
        carWithImage.put(carRepository.findCarById(id), Base64.getEncoder().encodeToString(carRepository.findCarById(id).getImgData()));
        mav.addObject("car", carWithImage);
        List<Review> reviews = reviewRepository.getReviewsOfCar(id);
        mav.addObject("reviews", reviews);
        mav.addObject("noRatings", reviews.size());
        return mav;
    }

//    @GetMapping("/pricing")
//    public ModelAndView showPricingPage() {
//        ModelAndView mav = new ModelAndView("pricing");
//        int total = 0;
//        int counter = 0;
//        HashMap<Car, String> carsWithImage = new HashMap();
//        for (Car car : carRepository.getListOfCars()) {
//            for (Integer rating : reviewRepository.getRatingsOfCar(car.getId())) {
//                total += rating;
//                counter ++;
//            }
//            if (counter == 0) {
//                car.setRating(0);
//            } else {
//                car.setRating((int)Math.ceil(total/counter));
//            }
//            total = 0;
//            counter = 0;
//            carsWithImage.put(car, Base64.getEncoder().encodeToString(car.getImgData()));
//        }
//        mav.addObject("cars", carsWithImage);
//        return mav;
//    }

    @GetMapping("/pricing/{field}")
    public ModelAndView showPricingPage(@PathVariable("field") String field) {
        ModelAndView mav = new ModelAndView("pricing");
        int total = 0;
        int counter = 0;
        List<Car> carList = carService.findAllWithSort(field);
        for (Car car : carList) {
            for (Integer rating : reviewRepository.getRatingsOfCar(car.getId())) {
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
            car.setImgString(Base64.getEncoder().encodeToString(car.getImgData()));
        }
        mav.addObject("cars", carList);
        return mav;
    }

    private double[] priceRangeParam(String priceRangeOptions) {
        double[] priceRange = new double[2];

        if (priceRangeOptions == null) {
            priceRangeOptions = "null";
        }

        switch (priceRangeOptions) {
            case "50to75" -> {
                priceRange[0] = 50;
                priceRange[1] = 75;
            }
            case "76to100" -> {
                priceRange[0] = 76;
                priceRange[1] = 100;
            }
            case "101to125" -> {
                priceRange[0] = 101;
                priceRange[1] = 125;
            }
            case "126to150" -> {
                priceRange[0] = 126;
                priceRange[1] = 150;
            }
            case "151plus" -> {
                priceRange[0] = 151;
                priceRange[1] = 1000000;
            }
            default -> {
                priceRange[1] = 1000000;
            }
        }
        return priceRange;
    }

    private String transmissionParams(String transmissionOptions) {
        if (transmissionOptions == null || transmissionOptions.equals("null")) {
            transmissionOptions = "%";
        }

        return transmissionOptions;
    }

    private int[] mileageParams(String mileageOptions) {
        int[] mileage = new int[2];

        if (mileageOptions == null) {
            mileageOptions = "null";
        }

        switch (mileageOptions) {
            case "less10000" -> {
                mileage[1] = 10000;
            }
            case "10001to30000" -> {
                mileage[0] = 10001;
                mileage[1] = 30000;
            }
            case "30001to60000" -> {
                mileage[0] = 30001;
                mileage[1] = 60000;
            }
            case "60001to100000" -> {
                mileage[0] = 600001;
                mileage[1] = 100000;
            }
            case "more100000" -> {
                mileage[0] = 100001;
                mileage[1] = 1000000;
            }
            default -> {
                mileage[1] = 1000000;
            }
        }

        return mileage;
    }

    private String fuelTypeParams(String fuelTypeOptions) {
        if (fuelTypeOptions == null || fuelTypeOptions.equals("null")) {
            fuelTypeOptions = "%";
        }

        return fuelTypeOptions;
    }

    private int[] seatsParams(String seatsOptions) {
        int[] seats = new int[2];

        if (seatsOptions == null) {
            seatsOptions = "null";
        }

        switch (seatsOptions) {
            case "2" -> {
                seats[0] = 2;
                seats[1] = 2;
            }
            case "3" -> {
                seats[0] = 3;
                seats[1] = 3;
            }
            case "4" -> {
                seats[0] = 4;
                seats[1] = 4;
            }
            case "5plus" -> {
                seats[0] = 5;
                seats[1] = 100;
            }
            default -> {
                seats[1] = 100;
            }
        }

        return seats;
    }

}
