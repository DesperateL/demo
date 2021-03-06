package com.example.demo.util;

import com.alibaba.fastjson.JSON;
import com.example.demo.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;


@Service
public class JedisAdapter implements InitializingBean {
    private JedisPool pool = null;
    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);



    //当前对象在初始化之后干嘛干嘛
    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("localhost",6379);

    }

    private Jedis getJedis(){
        return pool.getResource();
    }

    public long sadd(String key,String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.sadd(key,value);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally{
            if(jedis!=null){
                jedis.close();
            }
        }
    }
    public long srem(String key,String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.srem(key,value);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally{
            if(jedis!=null){
                jedis.close();
            }
        }
    }
    public boolean sismember(String key,String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.sismember(key,value);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return false;
        }finally{
            if(jedis!=null){
                jedis.close();
            }
        }
    }
    public long scard(String key){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.scard(key);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally{
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public void setObject(String key,Object obj){
        set(key, JSON.toJSONString(obj));//序列化
    }

    public <T> T getObject(String key,Class<T> clazz){
        String value = get(key);
        if(value!=null){
            return JSON.parseObject(value,clazz);//反序列化
        }
        return  null;
    }
    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return getJedis().get(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();

            jedis.set(key, value);
        } catch (Exception e) {
            logger.error("发生异常:" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public static void main(String[] args) {
        Jedis jedis = new Jedis();
        jedis.set("k10","10");
        User user = new User();
        user.setHeadUrl("http://image.nowcoder.com/head/100t.png");
        user.setName("user1");
        user.setPassword("password");
        user.setSalt("salt");

        new JedisAdapter().setObject("user1xx",user);
    }

}
