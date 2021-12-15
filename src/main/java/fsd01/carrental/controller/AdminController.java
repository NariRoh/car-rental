package fsd01.carrental.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fsd01.carrental.dtos.UserDTO;
import fsd01.carrental.entity.User;
import fsd01.carrental.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
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

        return mav;
    }

//    @RequestPart(value = )

}
