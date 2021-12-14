package fsd01.carrental.controller;

import fsd01.carrental.dtos.UserDTO;
import fsd01.carrental.dtos.UserUpdateDTO;
import fsd01.carrental.dtos.PasswordDTO;
import fsd01.carrental.entity.User;
import fsd01.carrental.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;

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
        modelAndView.addObject("userUpdateDTO", new UserUpdateDTO());
//        modelAndView.addObject("passwordDTO", new PasswordDTO());

        return modelAndView;

//        // TODO: redirect to 'access denied page'
//        return authentication == null ? "redirect:/" : "profile";
    }

    @PostMapping("/profile")
    public ModelAndView updateUserProfile(
            @ModelAttribute @Valid UserUpdateDTO userUpdateDTO,
            @ModelAttribute UserDTO userDTO,
            BindingResult bindingResult) {

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

        // TODO: add flash message when update success
        mav.setView(new RedirectView("profile"));
        return mav;
    }


//    @PostMapping ("/password")
//    public String updateUserPassword(
//            @Valid PasswordDTO passwordDTO,
////            @ModelAttribute UserDTO userDTO,
//            BindingResult bindingResult,
//            Model model) {
//
//        log.info(">>>>>> Requested password update fields  : {}", passwordDTO);
//
//        // check  if current password is equal to record
//        userService.validatePassword(passwordDTO, bindingResult);
//
//        if (bindingResult.hasErrors()) {
////            model.addAttribute("userDTO", userDTO);
//            return "profile";
//        }
//        // update password
//
//        return "redirect:/profile";
//    }


}
