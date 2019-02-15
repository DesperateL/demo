package com.example.demo.controller;

import com.example.demo.async.EventModel;
import com.example.demo.async.EventProducer;
import com.example.demo.async.EventType;
import com.example.demo.model.LoginTicket;
import com.example.demo.service.NewsService;
import com.example.demo.service.UserService;
import com.example.demo.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Controller
public class LoginController {
    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    NewsService newsService;

    @Autowired
    UserService userService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/reg"}, method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String reg(Model model, @RequestParam("username")String username,
                      @RequestParam("password")String password,
                      @RequestParam(value="rember",defaultValue = "0")int rememberme,
                      HttpServletResponse response) {
        try {
            Map<String,Object> map = userService.register(username,password);
            if(map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");//设置cookie全站有效
                if(rememberme>0){
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);

                return ToutiaoUtil.getJSONString(0,"注册成功");
            }else{
                return  ToutiaoUtil.getJSONString(1,map);
            }

        }catch(Exception e){
            logger.error("注册异常"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"注册异常");
        }

    }

    @RequestMapping(path = {"/login"}, method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String login(Model model, @RequestParam("username")String username,
                      @RequestParam("password")String password,
                      @RequestParam(value="rember",defaultValue = "0")int rememberme,
                        HttpServletResponse response) {
        try {
            Map<String,Object> map = userService.login(username,password);
            if(map.containsKey("ticket")){

                Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");//设置cookie全站有效
                if(rememberme>0){
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                //System.out.println("+++++++++++++++");
                eventProducer.fireEvent(new EventModel(EventType.LOGIN).setActorId((int)(map.get("userId")))
                        .setExt("username",username).setExt("email","747713425@qq.com"));
                //System.out.println("???????????????");
                return ToutiaoUtil.getJSONString(0,"登录成功");
            }else{
                return  ToutiaoUtil.getJSONString(1,map);
            }

        }catch(Exception e){
            logger.error("登录异常"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"登录失败");
        }

    }
    @RequestMapping(path = {"/logout"}, method = {RequestMethod.GET,RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        System.out.println("logout");
        return "redirect:/";
    }
}
