package fsd01.carrental.controller;

import fsd01.carrental.dtos.UserDTO;
import fsd01.carrental.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
public class AccountController {

    @Autowired
    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    // trim any whitespace characters then return to null
    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/register")
    public ModelAndView showRegisterView() {
        ModelAndView modelAndView = new ModelAndView("register");
        modelAndView.addObject("userDTO", new UserDTO());

        return modelAndView;
    }

    @PostMapping("/register")
    public String registerUser(@Valid UserDTO userDTO, BindingResult bindingResult) {

        // validates email uniqueness and phone number pattern
        userService.validateEmail(userDTO.getEmail(), bindingResult);
        if (userDTO.getPhoneNumber() != null) {
            userService.validatePhoneNumber(userDTO.getPhoneNumber(), bindingResult);
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }

        userService.createUser(userDTO);
        log.info(">>>>>> Creating a new user : {}", userDTO);

        return "redirect:/";
    }

    @GetMapping("/login")
    public String showLoginView(Authentication authentication) {
        return authentication == null ? "signIn" : "redirect:/";
    }
}
