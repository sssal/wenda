package com.nowcoder.controller;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

@Controller
public class IndexController {

    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String index(HttpSession httpSession) {
        return "Hello NowCoder" + httpSession.getAttribute("msg");
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

        return "home";
    }

    @RequestMapping(path = {"/request"}, method = {RequestMethod.GET})
    @ResponseBody
    public String request(Model model, HttpServletResponse response,
                          HttpServletRequest request,
                          HttpSession httpSession,
                          @CookieValue("JSESSIONID") String sessionId) {
        StringBuilder sb = new StringBuilder();
        sb.append("COOKIEVALUE" + sessionId + "<br>");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            sb.append(name + ":" + request.getHeader(name) + "<br>");
        }
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                sb.append("Cookie:" + cookie.getName() + " value:" + cookie.getValue());
            }
        }
        sb.append(request.getMethod()).append("<br>");
        sb.append(request.getQueryString() + "<br>");
        sb.append(request.getPathInfo() + "<br>");
        sb.append(request.getRequestURI() + "<br>");

        response.addHeader("sssId", "Hello");
        response.addCookie(new Cookie("username", "sss"));

        return sb.toString();
    }

    @RequestMapping(path = {"/redirect/{code}"}, method = {RequestMethod.GET})
//    @ResponseBody
    public RedirectView redirect(@PathVariable("code") int code,
                                 HttpSession httpSession) {
        httpSession.setAttribute("msg", " jump from redirect");
//        return "redirect:/";
        RedirectView redirectView = new RedirectView("/", true);
        if (code == 301) {
            redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return redirectView;
    }

    @RequestMapping(path = {"/admin"}, method = {RequestMethod.GET})
    @ResponseBody
    public String admin(@RequestParam("key") String key) {
        if ("admin".equals(key)) {
            return "hello admin";
        }
        throw new IllegalArgumentException("参数不对");
    }

    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e) {
        return "error" + e.getMessage();
    }
}
