package com.niuke.forum.controller;

import com.niuke.forum.async.EventModel;
import com.niuke.forum.async.EventProducer;
import com.niuke.forum.async.EventType;
import com.niuke.forum.model.Comment;
import com.niuke.forum.model.EntityType;
import com.niuke.forum.model.HostHolder;
import com.niuke.forum.service.CommentService;
import com.niuke.forum.service.LikeService;
import com.niuke.forum.util.ForumUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {
    private static final Logger logger = LoggerFactory.getLogger(LikeController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    @Autowired
    EventProducer eventProducer;

    @Autowired
    CommentService commentService;

    @PostMapping(path = "/like")
    @ResponseBody
    public String like(@RequestParam("commentId") int commentId) {
        if (hostHolder.getUser() == null) {
            return ForumUtil.getJsonString(999);
//            return "redirect:/reglogin";
        }

        Comment comment = commentService.getCommentById(commentId);

        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setActorId(hostHolder.getUser().getId()).setEntityId(commentId)
                .setEntityType(EntityType.ENTITY_COMMENT)
                .setExt("questionId", String.valueOf(comment.getEntityId()))
                .setEntityOwerId(comment.getUserId()));

        long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
        return ForumUtil.getJsonString(0, String.valueOf(likeCount));
    }

    @PostMapping(path = "/dislike")
    @ResponseBody
    public String dislike(@RequestParam("commentId") int commentId) {
        if (hostHolder.getUser() == null) {
            return ForumUtil.getJsonString(999);
//            return "redirect:/reglogin";
        }
        long likeCount = likeService.disLike(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
        return ForumUtil.getJsonString(0, String.valueOf(likeCount));
    }
}
