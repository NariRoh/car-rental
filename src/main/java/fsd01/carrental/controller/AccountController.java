package fsd01.carrental.controller;

import fsd01.carrental.entity.User;
import fsd01.carrental.object.UserDTO;
import fsd01.carrental.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
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

        // check if user email exists
        if (userService.userEmailExists(userDTO.getEmail())) {
            bindingResult.addError(new FieldError(
                    "userDTO", "email", "Email already in use"
            ));
        }
        // check if given phone number is in valid form
        if (userDTO.getPhoneNumber() != null) {
            if (!(userService.validatePhoneNumber(userDTO.getPhoneNumber()))) {
                bindingResult.addError(new FieldError(
                        "userDTO", "phoneNumber", "Enter a valid phone number"
                ));
            }
        }
        if (bindingResult.hasErrors()) {
            return "register";
        }

        userService.registerUser(userDTO);
        log.info(">>>>>> Creating a new user : {}", userDTO);

        return "redirect:/";
    }
//    public String registerUser(@Valid User user, BindingResult bindingResult) {
//        if (userService.userEmailExists(user.getEmail())) {
//            bindingResult.addError(new FieldError(
//                    "user", "email", "Email already in use"
//            ));
//        }
//        if (bindingResult.hasErrors()) {
//            return "register";
//        }
//
//        userService.saveUser(user);
//        log.info(">>>>>> Created new user : {}", user);
//
//        return "redirect:/";
//    }
}
