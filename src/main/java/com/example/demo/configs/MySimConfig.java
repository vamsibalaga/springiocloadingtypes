package com.example.demo.configs;

import com.example.demo.implementations.BoostMobile;
import com.example.demo.interfaces.ISim;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

//@ComponentScan("com.example.demo.configs")
public class MySimConfig {

    @Bean
    public ISim sim(){
        System.out.println("MySimConfig.sim() Constructor");
        return new BoostMobile();
    }
}
