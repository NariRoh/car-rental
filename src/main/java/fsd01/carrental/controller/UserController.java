package fsd01.carrental.controller;

import fsd01.carrental.dtos.UserDTO;
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

    @GetMapping("/profile")
    public String showUserProfileView(Authentication authentication) {
        // TODO: redirect to 'no access page'?
        return authentication == null ? "redirect:/" : "profile";
    }

    @PostMapping("/profile")
    public String updateUserProfile(
            @AuthenticationPrincipal User user,
            @Valid UserDTO userDTO,
            BindingResult bindingResult) {
        System.out.println(userDTO.toString());

        // check if user email exists
//        if (userService.userEmailExists(userDTO.getEmail())) {
//            bindingResult.addError(new FieldError(
//                    "userDTO", "email", "Email already in use"
//            ));
//        }
        // check if given phone number is in valid form
//        if (userDTO.getPhoneNumber() != null) {
//            if (!(userService.validatePhoneNumber(userDTO.getPhoneNumber()))) {
//                bindingResult.addError(new FieldError(
//                        "userDTO", "phoneNumber", "Enter a valid phone number"
//                ));
//            }
//        }
        if (bindingResult.hasErrors()) {
            return "profile";
        }
        userService.updateUser(user.getId(), userDTO);
        return "profile";
    }

//    @PostMapping("/validateFullName")
//    public String validateFullName(@RequestBody User user, Model model) {
//
//    }

}
