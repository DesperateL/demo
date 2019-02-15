package com.example.demo.async.handler;

import com.example.demo.async.EventHandler;
import com.example.demo.async.EventModel;
import com.example.demo.async.EventType;
import com.example.demo.model.Message;
import com.example.demo.model.User;
import com.example.demo.service.MessageService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class LikeHandler implements EventHandler {

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;
    @Override
    public void doHandle(EventModel model) {
        System.out.println("Liked");
        Message message = new Message();
        message.setFromId(3);//3-系统
        message.setToId(model.getEntityOwnerId());//应该是给
        User user  =userService.getUser(model.getActorId());
        System.out.println(user.getId()+":"+user.getName());
        message.setContent("用户"+user.getName()
                +"赞了你的资讯，http://127.0.0.1:8080/news/"+model.getEntityId());

        message.setConversationId(Math.min(message.getFromId(),message.getToId())+"_"+Math.max(message.getFromId(),message.getToId()));
        message.setCreatedDate(new Date());
        messageService.addMessage(message);
        System.out.println("Liked end");
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
