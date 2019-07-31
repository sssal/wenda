package com.niuke.forum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


public class IndexController {
    @RequestMapping(path = {"/"})
    @ResponseBody
    public String index() {
        return "Hello forum";
    }
}
