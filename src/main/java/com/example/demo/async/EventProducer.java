package com.example.demo.async;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.util.JedisAdapter;
import com.example.demo.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventProducer {  //发送事件到队列
    @Autowired
    JedisAdapter jedisAdapter;

    public  boolean fireEvent(EventModel model){
        try{
            //1.事件先序列化
            String json = JSONObject.toJSONString(model);
            //2.放到优先级队列去
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key,json);
            return true;
        }catch (Exception e){
            return  false;
        }
    }
}
