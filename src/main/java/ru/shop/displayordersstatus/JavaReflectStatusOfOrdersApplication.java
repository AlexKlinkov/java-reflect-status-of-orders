package ru.shop.displayordersstatus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableAsync
public class JavaReflectStatusOfOrdersApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(JavaReflectStatusOfOrdersApplication.class);
        app.run(args);
    }
}