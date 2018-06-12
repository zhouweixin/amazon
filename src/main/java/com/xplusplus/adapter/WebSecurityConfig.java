package com.xplusplus.adapter;

import com.xplusplus.exception.AmazonException;
import com.xplusplus.exception.EnumException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 16:45 2018/4/27
 * @Modified By:
 */
@Configuration
public class WebSecurityConfig extends WebMvcConfigurerAdapter {
    public final static String SESSION_USER = "user";

    @Bean
    public SecurityInterceptor getSecurityInterceptor() {
        return new SecurityInterceptor();
    }

    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor = registry.addInterceptor(getSecurityInterceptor());

        // 登录不拦截
        addInterceptor.excludePathPatterns("/user/login**");

        // 拦截所有
        addInterceptor.addPathPatterns("/**");
    }

    private class SecurityInterceptor extends HandlerInterceptorAdapter {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
            HttpSession session = request.getSession();

            // 判断是否已有该用户登录的session
            if (session.getAttribute(SESSION_USER) != null) {
                return true;
            }

            System.out.println("*************************没有登录, 跳转到登录页!*****************************");
            String url = "/amz-cube/login.html";
            response.sendRedirect(url);
            return false;
        }
    }
}
