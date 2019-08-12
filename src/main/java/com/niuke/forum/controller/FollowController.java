package com.niuke.forum.controller;

import com.niuke.forum.async.EventModel;
import com.niuke.forum.async.EventProducer;
import com.niuke.forum.async.EventType;
import com.niuke.forum.model.EntityType;
import com.niuke.forum.model.HostHolder;
import com.niuke.forum.model.Question;
import com.niuke.forum.service.FollowService;
import com.niuke.forum.service.QuestionService;
import com.niuke.forum.util.ForumUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @PostMapping(path = {"/followUser"})
    @ResponseBody
    public String unfollow(@RequestParam("userId") int userId) {
        if (hostHolder.getUser() == null) {
            return ForumUtil.getJsonString(999);
        }

        boolean ret = followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
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


        boolean ret = followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, questionId);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityId(questionId).setEntityType(EntityType.ENTITY_QUESTION)
                .setEntityOwerId(q.getUser_id()));

        return ForumUtil.getJsonString(ret ? 0 : 1
                , String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION)));
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
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityId(questionId).setEntityType(EntityType.ENTITY_QUESTION)
                .setEntityOwerId(q.getUser_id()));

        return ForumUtil.getJsonString(ret ? 0 : 1
                , String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION)));
    }

}
