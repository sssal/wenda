package com.niuke.forum.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

//import java.util.logging.Logger;

@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET})
    public String index(HttpSession httpSession) {
        return "index";
    }
}
