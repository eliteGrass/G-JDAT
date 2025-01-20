package com.liteGrass.springSecurity.abac.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @Company Huahui Information Technology Co., LTD.
 * @Author elitegrass
 * @Date 2024-12-26  22:15
 * @Description 配置信息
 */
@Configurable
@EnableWebSecurity
public class SpringSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(request -> {
            request.requestMatchers("/hello", "/test").permitAll()
                    .anyRequest().authenticated();
        })
                .formLogin(Customizer.withDefaults())

                .build();
    }

}
