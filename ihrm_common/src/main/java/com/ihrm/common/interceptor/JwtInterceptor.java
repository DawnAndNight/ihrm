package com.ihrm.common.interceptor;

import com.ihrm.common.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//自定义拦截器
public class JwtInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private JwtUtils jwtUtils;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取token token中获取claims 将claims 绑定到request中
        String authorization = request.getHeader("Authorization");
        //是否为空
        if(!StringUtils.isEmpty(authorization) && authorization.startsWith("Bearer ")){
            String token = authorization.replace("Bearer ","");
            Claims claims = jwtUtils.parseJWT(token);
            if(claims != null){
                String api = (String)claims.get("apis");
                //通过handler
                HandlerMethod h = (HandlerMethod) handler;
                RequestMapping annotation = h.getMethodAnnotation(RequestMapping.class);
                String name = annotation.name();
                if (api.contains(name)) {
                    request.setAttribute("user_claims",claims);
                    return true;
                }
            }
        }
        throw new Exception();
    }
}
