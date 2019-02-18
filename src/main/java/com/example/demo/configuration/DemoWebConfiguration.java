package com.example.demo.configuration;

import com.example.demo.interceptor.LoginRequiredtInterceptor;
import com.example.demo.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

//注册拦截器，要不然不知道要调用拦截器
@Component
public class DemoWebConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    PassportInterceptor passportInterceptor;

    @Autowired
    LoginRequiredtInterceptor loginRequiredtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);
        registry.addInterceptor(loginRequiredtInterceptor).addPathPatterns("/setting*");
        super.addInterceptors(registry);
    }
}
