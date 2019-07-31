package com.niuke.forum.dao;

import com.niuke.forum.model.Question;
import com.niuke.forum.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface QuestionDAO {
    String TABLE_NAME = " question ";
    String TABLE_FIELDS = " title, content, created_date, user_id, comment_count ";
    String SELECT_FIELDS = " id, " + TABLE_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", TABLE_FIELDS, ") values(#{title}, #{content}, #{created_date}, #{user_id}, #{comment_count})"})
    int addQuestion(Question question);

    @Select({"select ", SELECT_FIELDS, "from", TABLE_NAME, "where id=#{id}"})
    Question selectQuestionById(int id);

    //xml 配置mybatis
    List<Question> selectLatestQuestions(@Param("user_id") int user_id,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);

    @Update({"update", TABLE_NAME, "set comment_count = #{comment_count} where id = #{id}"})
    int updateCommentCount(int id, int comment_count);

}
