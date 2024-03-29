package com.niuke.forum.service;

import com.niuke.forum.dao.QuestionDAO;
import com.niuke.forum.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class QuestionService {
    @Autowired
    QuestionDAO questionDAO;

    @Autowired
    SensitiveService sensitiveService;

    public int addQuestion(Question question) {
        //敏感词过滤
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
//        questionDAO.addQuestion(question);
        question.setContent(sensitiveService.filter(question.getContent()));
        question.setTitle(sensitiveService.filter(question.getTitle()));

        return questionDAO.addQuestion(question) > 0 ? question.getId() : 0;

    }

    public Question selectQuestionById(int id) {
        return questionDAO.selectQuestionById(id);
    }

    public List<Question> getLatestQuestions(int user_id, int offset, int limit) {
        return questionDAO.selectLatestQuestions(user_id, offset, limit);
    }


    public int updateCommentCount(int id, int count) {
        return questionDAO.updateCommentCount(id, count);
    }
}
