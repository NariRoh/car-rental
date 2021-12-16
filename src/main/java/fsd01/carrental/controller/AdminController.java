package fsd01.carrental.controller;

import fsd01.carrental.dtos.UserUpdateDTO;
import fsd01.carrental.entity.User;
import fsd01.carrental.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

//@AllArgsConstructor
@RequestMapping("/admin")
@Controller
public class AdminController {

    @Autowired
    private UserService userService;
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

}
