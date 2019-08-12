package com.niuke.forum;

import com.niuke.forum.dao.QuestionDAO;
import com.niuke.forum.dao.UserDAO;
import com.niuke.forum.model.EntityType;
import com.niuke.forum.model.Question;
import com.niuke.forum.model.User;
import com.niuke.forum.service.FollowService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql("/init-schema.sql")
public class InitDatabaseTests {
    @Autowired
    UserDAO userDAO;

    @Autowired
    QuestionDAO questionDAO;

    @Autowired
    FollowService followService;

    @Test
    public void initDatabase() {
        Random random = new Random();

        for (int i = 0; i < 11; i++) {
            User user = new User();
            user.setHead_url(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setName(String.format("User%d", i + 1));
            user.setPassword("XX");
            user.setSalt("");
            userDAO.addUser(user);

            //互相关注
            for (int j = 1; j < i; j++) {
                followService.follow(j, EntityType.ENTITY_COMMENT, i);
            }

            Question question = new Question();
            question.setComment_count(i);
            Date date = new Date();
            date.setTime(date.getTime() + 1000 * 3600 * i);
            question.setCreated_date(date);
            question.setUser_id(i + 1);
            question.setTitle(String.format("TITLE{%d}", i));
            question.setContent(String.format("sssssss Content %d", i));
            questionDAO.addQuestion(question);
        }
        System.out.print(questionDAO.selectLatestQuestions(0, 0, 10));
    }
}
