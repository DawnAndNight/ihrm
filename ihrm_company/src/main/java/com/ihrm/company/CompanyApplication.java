package com.ihrm.company;

import com.ihrm.common.utils.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

//企业微服务
//配置包扫描
@SpringBootApplication(scanBasePackages = "com.ihrm")
//jpa注解扫描
@EntityScan(value = "com.ihrm")
//注册到eureka
@EnableEurekaClient
public class CompanyApplication {
//启动方法
    public static void main(String[] args) {
        SpringApplication.run(CompanyApplication.class,args);
    }
    @Bean
    public IdWorker idWorker(){
        return new IdWorker();
    }
}
