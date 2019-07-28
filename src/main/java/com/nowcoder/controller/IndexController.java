package com.nowcoder.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
public class IndexController {

    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String index() {
        return "Hello NowCoder";
    }

    @RequestMapping(path = {"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("userId") int userId,
                          @PathVariable("groupId") String groupId,
                          @RequestParam(value = "type", defaultValue = "0") int type,
                          @RequestParam(value = "key", required = false) String key) {
//        String s = String.format("sdfs %s","format");
        return String.format("Profile Page of %s / %d, t:%d k: %s", groupId, userId, type, key);
    }

    @RequestMapping(path = {"/home"}, method = {RequestMethod.GET})
    public String home(Model model) {
        model.addAttribute("value1", "vvvv1");
        List<String> colors = Arrays.asList("Red", "Green", "Blue");
        model.addAttribute("colors", colors);

        return "home.ftl";
    }
}
