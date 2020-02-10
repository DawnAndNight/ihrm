package com.ihrm.system.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Configuration
public class FeignCofiguration {
    @Bean
    public RequestInterceptor requestInterceptor(){
        return new RequestInterceptor() {
            @Override
            //将所有浏览器发送的请求头，赋值到feign中
            public void apply(RequestTemplate requestTemplate) {
                //获取请求头
                ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
                if(attributes != null){
                    HttpServletRequest request = attributes.getRequest();
                    Enumeration<String> hearName = request.getHeaderNames();
                    if(hearName != null){
                        while (hearName.hasMoreElements()){
                            String name = hearName.nextElement();
                            String value = request.getHeader(name);
                            requestTemplate.header(name,value);
                        }
                    }
                }

            }
        };
    }
}
