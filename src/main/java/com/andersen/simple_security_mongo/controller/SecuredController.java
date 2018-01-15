package com.andersen.simple_security_mongo.controller;

import com.andersen.simple_security_mongo.service.UserService;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class SecuredController {

    private static final Logger log = LoggerFactory.getLogger(SecuredController.class);

    @Autowired
    private UserService userService;

    @RequestMapping("/users")
    public String helloAdmin() {
        return "Users:<br>" + userService.getAllUsers();
    }


    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public @ResponseBody
    String createUser(@RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam("roles") String roles) {
        log.debug("POST Params: username="+ username + ", password="+ password +", roles=" + roles);
        userService.createUser(username, password, Arrays.stream(roles.substring(1, roles.length()-1).split(",")).collect(Collectors.toList()));
        return Optional.ofNullable(userService.getUserByUsername(username)).orElse(new JSONObject()).toString();
    }

    @RequestMapping(value = "/hello_admin", method = RequestMethod.GET)
    public String helloAdminPage() {
        return "hello_admin";
    }

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String helloPage() {
        return "hello";
    }

    @RequestMapping(value = "/my_login", method = RequestMethod.GET)
    public String loginPage() {
        return "my_login";
    }
}
