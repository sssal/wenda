package com.nowcoder.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @RequestMapping("/")

    @ResponseBody
    public String index(){
        return "Hello NowCoder";
    }

//    @RequestMapping(path = {"/profile/{userId}"})
//    @ResponseBody
//    public String profile(@PathVariable("userId") int userId){
//        return String.format("Profile Page of %d", userId);
//    }
}
