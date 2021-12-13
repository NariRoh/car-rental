package fsd01.carrental.controller;

import fsd01.carrental.dtos.UserDTO;
import fsd01.carrental.dtos.UserUpdateDTO;
import fsd01.carrental.entity.User;
import fsd01.carrental.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@AllArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @GetMapping("/profile")
    public ModelAndView showUserProfileView(@AuthenticationPrincipal User user) {
        ModelAndView modelAndView = new ModelAndView("profile");
        UserDTO userDTO = userService.getUserDTO(user.getId());

        log.info(">>>>>> Fetched user info : {}", userDTO);
        modelAndView.addObject("userDTO", userDTO);

        return modelAndView;

//        // TODO: redirect to 'access denied page'
//        return authentication == null ? "redirect:/" : "profile";
    }

    @PostMapping("/profile")
    public String updateUserProfile(
            @Valid UserUpdateDTO userUpdateDTO,
            @ModelAttribute UserDTO userDTO,
            Model model,
            BindingResult bindingResult) {

        log.info(">>>>>> Requested user update field(s) : {}", userUpdateDTO);

        // validates phone number pattern if it's changed
        if (userUpdateDTO.getPhoneNumber() != null) {
            userService.validatePhoneNumber(userUpdateDTO.getPhoneNumber(), bindingResult);
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("userDTO", userDTO);
            return "profile";
        }

        // FIXME: Validation failed for object='userUpdateDTO'. Error count: 1
        // https://stackoverflow.com/questions/16122257/how-to-pass-two-objects-to-use-in-a-form-using-thymeleaf
        // FIXME: password required when updating (setting to null for now)

        userService.updateUser(userUpdateDTO);

        // TODO: add flash message when update success
        return "redirect:/profile";
    }

//    @PostMapping("/validateFullName")
//    public String validateFullName(@RequestBody User user, Model model) {
//
//    }

}
