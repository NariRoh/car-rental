package fsd01.carrental.controller;

import fsd01.carrental.dtos.UserDTO;
import fsd01.carrental.dtos.UserUpdateDTO;
import fsd01.carrental.dtos.PasswordDTO;
import fsd01.carrental.entity.Booking;
import fsd01.carrental.entity.User;
import fsd01.carrental.repository.BookingRepository;
import fsd01.carrental.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.util.List;

@Controller
@AllArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private final BookingRepository bookingRepository;

    @GetMapping("/profile")
    public ModelAndView showUserProfileView(@AuthenticationPrincipal User user) {
        //  TODO: set 'access denied' for not logged-in access
        ModelAndView mav = new ModelAndView("profile");
        UserDTO userDTO = userService.getUserDTO(user.getId());

        log.info(">>>>>> Fetched user info : {}", userDTO);
        mav.addObject("userDTO", userDTO);
        mav.addObject("userUpdateDTO", new UserUpdateDTO());
        mav.addObject("passwordDTO", new PasswordDTO());

        return mav;
    }

    @PostMapping("/profile")
    public ModelAndView updateUserProfile(
            UserDTO userDTO,
            @ModelAttribute @Valid UserUpdateDTO userUpdateDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        log.info(">>>>>> Requested user update field(s) : {}", userUpdateDTO);
        ModelAndView mav = new ModelAndView();

        // validates phone number pattern if it's changed
        if (userUpdateDTO.getPhoneNumber() != null) {
            userService.validatePhoneNumber(userUpdateDTO.getPhoneNumber(), bindingResult);
        }

        if (bindingResult.hasErrors()) {
            mav.setViewName("profile");
            mav.addObject("userDTO", userDTO);
            return mav;
        }

        userService.updateUser(userUpdateDTO);

        // send success message
        redirectAttributes.addFlashAttribute(
                "message",
                "Your profile updated successfully!");

        mav.setView(new RedirectView("profile"));
        return mav;
    }

    @GetMapping("/password")
    public ModelAndView showUserPasswordView() {
        //  TODO: set 'access denied' for not logged-in access
        ModelAndView mav = new ModelAndView("password");
        mav.addObject("passwordDTO", new PasswordDTO());

        return mav;
    }
    @PostMapping ("/password")
    public String updateUserPassword(
            @ModelAttribute UserDTO userDTO,
            @ModelAttribute @Valid PasswordDTO passwordDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        // NOTE: binding result should be immediate to @Valid object otherwise it throws
        // Field error in object ....

        log.info(">>>>>> Requested password update fields  : {}", passwordDTO);

        // check  if current password matches to record
        userService.validatePassword(passwordDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return "password";
        }
        // update password
        userService.updatePassword(passwordDTO);

        // send success message
        redirectAttributes.addFlashAttribute(
                "message",
                "Your password updated successfully!");
        return "redirect:/password";
    }

    @GetMapping("/past-bookings")
    public ModelAndView showPastBookings(@AuthenticationPrincipal User user) {
        ModelAndView mav = new ModelAndView("past-bookings");
        List<Booking> pastBookings = bookingRepository.getListOfPastBookings(user.getId());
        mav.addObject("bookings", pastBookings);

        return mav;
    }

    @GetMapping("/upcoming-bookings")
    public ModelAndView showUpcomingBookings(@AuthenticationPrincipal User user) {
        ModelAndView mav = new ModelAndView("upcoming-bookings");
        List<Booking> upcomingBookings = bookingRepository.getListOfBookings();
        mav.addObject("bookings", bookingRepository.getListOfUpcomingBookingsByUser(user.getId()));

        return mav;
    }
}
