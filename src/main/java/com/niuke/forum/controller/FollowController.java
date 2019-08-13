package com.niuke.forum.controller;

import com.niuke.forum.async.EventModel;
import com.niuke.forum.async.EventProducer;
import com.niuke.forum.async.EventType;
import com.niuke.forum.model.*;
import com.niuke.forum.service.CommentService;
import com.niuke.forum.service.FollowService;
import com.niuke.forum.service.QuestionService;
import com.niuke.forum.service.UserService;
import com.niuke.forum.util.ForumUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FollowController {
    private static final Logger logger = LoggerFactory.getLogger(FollowController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    FollowService followService;

    @Autowired
    EventProducer eventProducer;

    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @PostMapping(path = {"/followUser"})
    @ResponseBody
    public String follow(@RequestParam("userId") int userId) {
        if (hostHolder.getUser() == null) {
            return ForumUtil.getJsonString(999);
        }

        boolean ret = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityId(userId).setEntityType(EntityType.ENTITY_USER)
                .setEntityOwerId(userId));

        return ForumUtil.getJsonString(ret ? 0 : 1
                , String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(), EntityType.ENTITY_USER)));
    }

    @PostMapping(path = {"/unfollowUser"})
    @ResponseBody
    public String unfollowUser(@RequestParam("userId") int userId) {
        if (hostHolder.getUser() == null) {
            return ForumUtil.getJsonString(999);
        }

        boolean ret = followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId);
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityId(userId).setEntityType(EntityType.ENTITY_USER)
                .setEntityOwerId(userId));

        return ForumUtil.getJsonString(ret ? 0 : 1
                , String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(), EntityType.ENTITY_USER)));
    }

    @PostMapping(path = {"/followQuestion"})
    @ResponseBody
    public String followQuestion(@RequestParam("questionId") int questionId) {
        if (hostHolder.getUser() == null) {
            return ForumUtil.getJsonString(999);
        }

        Question q = questionService.selectQuestionById(questionId);
        if (q == null) {
            return ForumUtil.getJsonString(1, "问题不存在");
        }


        boolean ret = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, questionId);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityId(questionId).setEntityType(EntityType.ENTITY_QUESTION)
                .setEntityOwerId(q.getUser_id()));

        Map<String, Object> info = new HashMap<String, Object>();
        info.put("headUrl", hostHolder.getUser().getHead_url());
        info.put("name", hostHolder.getUser().getName());
        info.put("id", hostHolder.getUser().getId());
        info.put("count", followService.getFolloweeCount(EntityType.ENTITY_QUESTION, questionId));

        return ForumUtil.getJsonString(ret ? 0 : 1, info);
    }

    @PostMapping(path = {"/unfollowQuestion"})
    @ResponseBody
    public String unfollowQuestion(@RequestParam("questionId") int questionId) {
        if (hostHolder.getUser() == null) {
            return ForumUtil.getJsonString(999);
        }

        Question q = questionService.selectQuestionById(questionId);
        if (q == null) {
            return ForumUtil.getJsonString(1, "问题不存在");
        }

        boolean ret = followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, questionId);
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityId(questionId).setEntityType(EntityType.ENTITY_QUESTION)
                .setEntityOwerId(q.getUser_id()));

        Map<String, Object> info = new HashMap<String, Object>();
        info.put("headUrl", hostHolder.getUser().getHead_url());
        info.put("name", hostHolder.getUser().getName());
        info.put("id", hostHolder.getUser().getId());
        info.put("count", followService.getFolloweeCount(EntityType.ENTITY_QUESTION, questionId));

        return ForumUtil.getJsonString(ret ? 0 : 1, info);
    }

    @GetMapping(value = "/user/{uid}/followers")
    public String followers(Model model, @PathVariable("uid") int userId) {
        List<Integer> followerIds = followService.getFollowers(EntityType.ENTITY_USER, userId, 0, 10);
        if (hostHolder.getUser() != null) {
            model.addAttribute("followers", getUsersInfo(hostHolder.getUser().getId(), followerIds));
        } else {
            model.addAttribute("followers", getUsersInfo(0, followerIds));
        }
        model.addAttribute("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));
        model.addAttribute("curUser", userService.getUser(userId));
        return "followers";
    }

    @GetMapping(value = "/user/{uid}/followees")
    public String followees(Model model, @PathVariable("uid") int userId) {
        List<Integer> followeeIds = followService.getFollowees(userId, EntityType.ENTITY_USER, 0, 10);

        if (hostHolder.getUser() != null) {
            model.addAttribute("followees", getUsersInfo(hostHolder.getUser().getId(), followeeIds));
        } else {
            model.addAttribute("followees", getUsersInfo(0, followeeIds));
        }
        model.addAttribute("followeeCount", followService.getFolloweeCount(userId, EntityType.ENTITY_USER));
        model.addAttribute("curUser", userService.getUser(userId));
        return "followees";
    }

    private List<ViewObject> getUsersInfo(int localUserId, List<Integer> userIds) {
        List<ViewObject> userInfos = new ArrayList<>();
        for (Integer uid : userIds) {
            User user = userService.getUser(uid);
            if (user == null) {
                continue;
            }
            ViewObject vo = new ViewObject();
            vo.set("user", user);
            vo.set("commentCount", commentService.getUserCommentCount(uid));
            vo.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, uid));
            vo.set("followeeCount", followService.getFolloweeCount(uid, EntityType.ENTITY_USER));
            if (localUserId != 0) {
                vo.set("followed", followService.isFollower(localUserId, EntityType.ENTITY_USER, uid));
            } else {
                vo.set("followed", false);
            }
            userInfos.add(vo);
        }
        return userInfos;
    }

}
