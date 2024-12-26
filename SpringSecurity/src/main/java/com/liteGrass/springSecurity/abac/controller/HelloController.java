package com.liteGrass.springSecurity.abac.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description ceshi、
 * @Author liteGrass
 * @Date 2024/12/26 16:27
 */
@RestController
public class HelloController {

    @RequestMapping("hello")
    public String hello() {
        System.out.println("hello");
        return "hello";
    }

}
