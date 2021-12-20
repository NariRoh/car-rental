package fsd01.carrental.controller;

import fsd01.carrental.dtos.UserUpdateDTO;
import fsd01.carrental.entity.Booking;
import fsd01.carrental.entity.Car;
import fsd01.carrental.entity.Review;
import fsd01.carrental.entity.User;
import fsd01.carrental.service.BookingService;
import fsd01.carrental.service.CarService;
import fsd01.carrental.service.ReviewService;
import fsd01.carrental.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;

@AllArgsConstructor
@RequestMapping("/admin")
@Controller
public class AdminController {

    private UserService userService;
    private CarService carService;
    private BookingService bookingService;
    private ReviewService reviewService;

    @GetMapping("/users")
    public String showUserList(Model model) {
        model.addAttribute("user", new User());;
        return findUsersPaginated(1, model);
    }

    @GetMapping("/users/page/{pageNo}")
    public String findUsersPaginated(@PathVariable (value = "pageNo") int pageNo, Model model) {
        int pageSize = 10;

        Page<User> page = userService.findPaginated(pageNo, pageSize);
        List<User> listUsers = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("userList", listUsers);
        return "admin/user-board";
    }

    @RequestMapping(value = "/users/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteUser(@PathVariable("id") Long id) {
        userService.removeUser(id);
        return "redirect:admin/user-board";
    }

    // commented this out because we don't want admin manually create user
//    @RequestMapping(value = "/users/add", method = RequestMethod.POST)
//    @ResponseBody
//    public String addUser(@ModelAttribute User user) {
//        userService.createUser(user);
//        log.info(">>>>>> Creating a new user : {}", user);
//        return "redirect:admin/user-board";
//    }

    @RequestMapping(value = "/users/edit/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String updateUserInfo(@PathVariable("id") Long id, @RequestBody UserUpdateDTO userUpdateDTO) {
        userUpdateDTO.setId(id);
        userService.updateUser(userUpdateDTO);
        return "redirect:admin/user-board";
    }

    @GetMapping("/cars")
    public String showCarList(Model model) {
        model.addAttribute("car", new Car());
        return findCarsPaginated(1, model);
    }

    @GetMapping("/cars/page/{pageNo}")
    public String findCarsPaginated(@PathVariable (value = "pageNo") int pageNo, Model model) {
        int pageSize = 10;

        Page<Car> page = carService.findPaginated(pageNo, pageSize);
        List<Car> listCars = page.getContent();
        HashMap<Car, String> carsWithImage = new HashMap<>();
        for (Car car : listCars) {
            carsWithImage.put(car, Base64.getEncoder().encodeToString(car.getImgData()));
        }

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("carList", carsWithImage);
        return "admin/car-board";
    }

    @RequestMapping(value = "/cars/add", method = RequestMethod.POST)
    @ResponseBody
    public String addCar(@ModelAttribute Car car) {
        carService.createCar(car);
        return "redirect:admin/user-board";
    }

    @RequestMapping(value = "/cars/edit/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String updateCarInfo(@PathVariable("id") Long id, @RequestBody Car carObj) {
        carService.updateCar(id, carObj);
        return "redirect:admin/car-board";
    }

    @RequestMapping(value = "/cars/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteCar(@PathVariable("id") Long id) {
//        comment it out so we don't accidentally delete car data
//        carService.removeCar(id);
        return "redirect:admin/car-board";
    }

    @GetMapping("/reservations")
        public String showPastBookings(Model model) {
        model.addAttribute("booking", new Booking());
        return findBookingsPaginated(1, model);
    }

    @GetMapping("/reservations/page/{pageNo}")
        public String findBookingsPaginated(@PathVariable (value = "pageNo") int pageNo, Model model) {
        int pageSize = 10;

        Page<Booking> page = bookingService.findPaginated(pageNo, pageSize);
        List<Booking> listBookings = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("bookingList", listBookings);
        return "admin/booking-board";
    }

    @GetMapping("/reviews")
    public String showReviewList(Model model) {
        model.addAttribute("booking", new Booking());
        return findReviewsPaginated(1, model);
    }

    @GetMapping("/reviews/page/{pageNo}")
    public String findReviewsPaginated(@PathVariable (value = "pageNo") int pageNo, Model model) {
        int pageSize = 10;

        Page<Review> page = reviewService.findPaginated(pageNo, pageSize);
        List<Review> reviewList = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("reviewList", reviewList);
        return "admin/review-board";
    }

}
