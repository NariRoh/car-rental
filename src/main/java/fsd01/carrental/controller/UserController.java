package fsd01.carrental.controller;

import fsd01.carrental.entity.User;
import fsd01.carrental.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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

    @GetMapping("/login")
    public String showLoginView(Authentication authentication) {
        return authentication == null ? "signIn" : "redirect:/";
    }

    @GetMapping("/profile")
    public String showUserProfileView(Authentication authentication) {
        // TODO: redirect to 'no access page'?
        return authentication == null ? "redirect:/" : "profile";
    }

//    @PostMapping("/validateFullName")
//    public String validateFullName(@RequestBody User user, Model model) {
//
//    }

}
