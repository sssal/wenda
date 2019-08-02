package com.niuke.forum.service;

import com.niuke.forum.dao.QuestionDAO;
import com.niuke.forum.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {
    @Autowired
    QuestionDAO questionDAO;

    public List<Question> getLatestQuestions(int user_id, int offset, int limit) {
        return questionDAO.selectLatestQuestions(user_id, offset, limit);
    }
}
