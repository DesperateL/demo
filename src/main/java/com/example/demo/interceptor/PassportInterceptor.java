package com.example.demo.interceptor;


import com.example.demo.dao.LoginTicketDAO;
import com.example.demo.dao.UserDAO;
import com.example.demo.model.HostHolder;
import com.example.demo.model.LoginTicket;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

//拦截器，之前是否在登录状态
@Component
public class PassportInterceptor implements HandlerInterceptor {
    @Autowired
    private LoginTicketDAO loginTicketDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private HostHolder hostHolder;

    //请求处理之前，如果返回false，那么这个请求到这里就结束了，不进行后续处理
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String ticket = null;
        //从cookies里读取字段
        System.out.println("preHandle1");
        if(httpServletRequest.getCookies()!=null){
            for(Cookie cookie:httpServletRequest.getCookies()){
                if(cookie.getName().equals("ticket")){
                    ticket = cookie.getValue();
                    break;
                }
            }
        }
        System.out.println("preHandle2");
        if(ticket==null)return true;
        System.out.println("preHandle3");
        if(ticket!=null){
            LoginTicket loginTicket = loginTicketDAO.selectByTicket(ticket);
            if(loginTicket==null||loginTicket.getExpired().before(new Date())||loginTicket.getStatus()!=0){
                return true;
            }
            User user = userDAO.selectById(loginTicket.getUserId());
            hostHolder.setUsers(user);

        }
        System.out.println("preHandle4");
        return true;
    }
    //Controller渲染之前
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        //登录用户
        if(modelAndView!=null && hostHolder.getUsers()!=null){
            modelAndView.addObject("user",hostHolder.getUsers());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        hostHolder.clear();
    }
}
