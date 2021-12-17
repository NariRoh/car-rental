package fsd01.carrental.controller;

import fsd01.carrental.dtos.UserUpdateDTO;
import fsd01.carrental.entity.Booking;
import fsd01.carrental.entity.Car;
import fsd01.carrental.entity.User;
import fsd01.carrental.service.BookingService;
import fsd01.carrental.service.CarService;
import fsd01.carrental.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@AllArgsConstructor
@RequestMapping("/admin")
@Controller
public class AdminController {

    private UserService userService;
    private CarService carService;
    private BookingService bookingService;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    //  TODO: set 'access denied' for not logged-in & invalid access
    @GetMapping("users")
    public ModelAndView showUserList() {
        ModelAndView mav = new ModelAndView("admin/user-board");
        List<User> userList = userService.getUserList();
        mav.addObject("userList", userList);
        mav.addObject("user", new User());

        return mav;
    }

    @RequestMapping(value = "/users/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteUser(@PathVariable("id") Long id) {
        userService.removeUser(id);
        return "redirect:admin/user-board";
    }

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

    @GetMapping("cars")
    public ModelAndView showCarList() {
        ModelAndView mav = new ModelAndView("admin/car-board");
        List<Car> carList = carService.getCarList();

        mav.addObject("carList", carList);
        mav.addObject("car", new Car());

        return mav;
    }

    @RequestMapping(value = "/cars/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteCar(@PathVariable("id") Long id) {
        carService.removeCar(id);
        return "redirect:admin/car-board";
    }

    @GetMapping("reservations")
    public ModelAndView showReservationList() {
        ModelAndView mav = new ModelAndView("admin/booking-board");
        List<Booking> bookingList = bookingService.getBookingList();

        mav.addObject("bookingList", bookingList);
        mav.addObject("booking", new Booking());

        return mav;
    }

}
