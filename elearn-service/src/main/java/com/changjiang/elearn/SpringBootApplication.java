package com.changjiang.elearn;

import com.changjiang.grpc.annotation.EnableGrpcService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

@org.springframework.boot.autoconfigure.SpringBootApplication
@ComponentScan(basePackages = {
        "com.changjiang.elearn",
        "com.changjiang.grpc",
        "com.changjiang.python"
})
@MapperScan("com.changjiang.elearn.infrastructure.persistence.dao")
@EnableGrpcService
public class SpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootApplication.class, args);
    }
}

