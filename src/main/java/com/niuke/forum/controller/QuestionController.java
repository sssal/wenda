package com.niuke.forum.controller;

import com.niuke.forum.model.*;
import com.niuke.forum.service.CommentService;
import com.niuke.forum.service.LikeService;
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
import java.util.Date;
import java.util.List;

@Controller
public class QuestionController {
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    CommentService commentService;

    @Autowired
    LikeService likeService;
    @RequestMapping(path = "/question/add", method = {RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title,
                              @RequestParam("content") String content) {
        try {
            Question question = new Question();
            question.setContent(content);
            question.setTitle(title);
            question.setCreated_date(new Date());
            question.setComment_count(0);
            if (hostHolder.getUser() == null) {
                //未登陆
                question.setUser_id(ForumUtil.ANONYMOUS_USERID);
//                return ForumUtil.getJsonString(999);
            } else {
                question.setUser_id(hostHolder.getUser().getId());
            }

            if (questionService.addQuestion(question) > 0) {
                return ForumUtil.getJsonString(0);
            }
            ;

        } catch (Exception e) {
            logger.error("增加题目失败" + e.getMessage());
        }

        return ForumUtil.getJsonString(1, "失败");
    }

    @RequestMapping(path = "/question/{qid}", method = {RequestMethod.GET})
    public String questionDetail(Model model, @PathVariable("qid") int qid) {
        Question question = questionService.selectQuestionById(qid);
//        Question question = questionService.
        model.addAttribute("question", question);

        List<Comment> commentList = commentService.getCommentsByEntity(qid, EntityType.ENTITY_QUESTION);
        List<ViewObject> comments = new ArrayList<ViewObject>();
        for (Comment comment : commentList) {
            ViewObject vo = new ViewObject();
            vo.set("comment", comment);

            if (hostHolder.getUser() == null) {
                vo.set("liked", 0);
            } else {
                vo.set("liked", likeService.getLikeStatus(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, comment.getId()));
            }
            vo.set("likeCount", likeService.getLikeCount(EntityType.ENTITY_COMMENT, comment.getId()));
            vo.set("user", userService.getUser(comment.getUserId()));
            comments.add(vo);
        }
        model.addAttribute("comments", comments);
//        model.addAttribute("user", userService.getUser(question.getUser_id()));
        return "detail";
    }
}
