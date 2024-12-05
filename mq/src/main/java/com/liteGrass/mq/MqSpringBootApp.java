package com.liteGrass.mq;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description 启动类
 * @Author liteGrass
 * @Date 2024/12/5 20:01
 */
@SpringBootApplication
public class MqSpringBootApp implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(MqSpringBootApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Hello World");
    }
}
