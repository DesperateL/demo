package com.example.demo.controller;


import com.example.demo.async.EventModel;
import com.example.demo.async.EventProducer;
import com.example.demo.async.EventType;
import com.example.demo.model.EntityType;
import com.example.demo.model.HostHolder;
import com.example.demo.model.News;
import com.example.demo.service.LikeService;
import com.example.demo.service.NewsService;
import com.example.demo.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    @Autowired
    NewsService newsService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/like"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String like( @RequestParam ("newsId")int newsId){
        //System.out.println("like______________");
        long likeCount = likeService.like(hostHolder.getUsers().getId(),EntityType.ENTITY_NEWS,newsId);
        //更新喜欢数；
        News news = newsService.getById(newsId);
       // System.out.println(newsId+"______________"+likeCount);
        newsService.updateLikeCount(newsId,(int)likeCount);
        //System.out.println("like______________");

        //喜欢事件
        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setActorId(hostHolder.getUsers().getId())
                .setEntityId(newsId).setEntityType(EntityType.ENTITY_NEWS)
                .setEntityOwnerId(news.getUserId()));


        return ToutiaoUtil.getJSONString(0,String.valueOf(likeCount));
    }
    @RequestMapping(path = {"/dislike"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String dislike( @RequestParam ("newsId")int newsId){
        //System.out.println("dislike______________");
        int userId= hostHolder.getUsers().getId();
        long likeCount = likeService.dislike(userId, EntityType.ENTITY_NEWS,newsId);
        newsService.updateLikeCount(newsId,(int)likeCount);



        return ToutiaoUtil.getJSONString(0,String.valueOf(likeCount));
    }
}
