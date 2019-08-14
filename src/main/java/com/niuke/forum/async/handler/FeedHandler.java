package com.niuke.forum.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.niuke.forum.async.EventHandler;
import com.niuke.forum.async.EventModel;
import com.niuke.forum.async.EventType;
import com.niuke.forum.model.*;
import com.niuke.forum.service.FeedService;
import com.niuke.forum.service.MessageService;
import com.niuke.forum.service.QuestionService;
import com.niuke.forum.service.UserService;
import com.niuke.forum.util.ForumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class FeedHandler implements EventHandler {
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @Autowired
    FeedService feedService;

    private String buildFeedDate(EventModel model) {
        Map<String, String> map = new HashMap<>();
        User actor = userService.getUser(model.getActorId());
        if (actor == null) {
            return null;
        }
        map.put("userId", String.valueOf(actor.getId()));
        map.put("userHead", actor.getHead_url());
        map.put("userName", actor.getName());

        if (model.getType() == EventType.COMMENT
                || model.getType() == EventType.FOLLOW && model.getEntityType() == EntityType.ENTITY_QUESTION) {
            Question question = questionService.selectQuestionById(model.getEntityId());
            if (question == null) {
                return null;
            }
            map.put("questionId", String.valueOf(question.getId()));
            map.put("questionTitle", question.getTitle());
            return JSONObject.toJSONString(map);
        }
        return null;
    }

    @Override
    public void doHandle(EventModel model) {
        Feed feed = new Feed();
        feed.setCreateDate(new Date());
        feed.setUserDd(model.getActorId());
        feed.setType(model.getType().getValue());
        feed.setData(buildFeedDate(model));
        if (feed.getData() == null) {
            return;
        }
        feedService.addFeed(feed);

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.COMMENT, EventType.FOLLOW);
    }
}
