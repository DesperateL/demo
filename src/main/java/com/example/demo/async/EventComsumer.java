package com.example.demo.async;

import com.alibaba.fastjson.JSON;
import com.example.demo.controller.IndexController;
import com.example.demo.util.JedisAdapter;
import com.example.demo.util.RedisKeyUtil;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//取队列中的事件
@Service
public class EventComsumer implements InitializingBean, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(EventComsumer.class);
    private Map<EventType, List<EventHandler>> config = new HashMap<>();
    private ApplicationContext applicationContext;

    @Autowired
    JedisAdapter jedisAdapter;

    @Override
    public void afterPropertiesSet() throws Exception {
        //实现InitializingBean接口，在初始化时...
        //所有事件信息(实现EventHandler接口的)记录，做一个路由表
        //获取所有实现EventHandler接口的类
        Map<String,EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);

        if(beans!=null){
            for(Map.Entry<String,EventHandler> entry : beans.entrySet()){
                //每个handler可以处理的EventType对象
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();
                if(eventTypes!=null){
                    for(EventType type:eventTypes){
                        if(!config.containsKey(type)){
                            config.put(type, new ArrayList<>() );
                        }
                        // 注册每个事件的处理函数
                        config.get(type).add(entry.getValue());
                    }
                }

            }
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    String key = RedisKeyUtil.getEventQueueKey();
                    //阻塞队列，等待着取事件.返回值包含两个String,第一个是key，第二个是value
                    List<String> events = jedisAdapter.brpop(0,key);
                    for(String message:events){
                        if(message.equals(key)){
                            continue;
                        }
                        EventModel eventModel = JSON.parseObject(message,EventModel.class);
                        if(!config.containsKey(eventModel.getType())){
                            logger.error("不能识别的事件");
                            continue;
                        }
                        //Handler处理相应的事件
                        for(EventHandler handler:config.get(eventModel.getType())){
                            handler.doHandle(eventModel);
                        }
                    }
                }
            }
        });
        thread.start();
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }



}
