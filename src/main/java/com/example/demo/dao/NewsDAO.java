package com.example.demo.dao;


import com.example.demo.model.News;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface NewsDAO {
    String TABLE_NAME = "news";
    String INSERT_FIELDS = " title,link,image,like_count,comment_count," +
            "created_date,user_id";
    String SELECT_FIELDS = " id,"+INSERT_FIELDS;

    @Insert({"insert into ",TABLE_NAME,"(",INSERT_FIELDS,
            ") values (#{title},#{link},#{image},#{likeCount},#{commentCount},#{createdDate},#{userId})"})
    int addNews(News news);

    List<News> selectByUserIdAndOffset(@Param(value = "userId") int userId, @Param(value = "offset") int offset, @Param(value = "limit") int limit);

    @Select(value = "select * from news")
    List<News> selectAll();

    @Select(value = "select * from news where id=#{newsId}")
    News getById(int newsId);

    @Update({"update ",TABLE_NAME," set comment_count=#{count} where id=#{entityId}"})
    void updateCommentCount(@Param("entityId") int entityId,@Param("count") int count);

    @Update({"update ",TABLE_NAME," set like_count=#{likeCount} where id=#{newsId}"})
    void updateLikeCount( @Param("likeCount") int likeCount,@Param("newsId") int newsId);
}
