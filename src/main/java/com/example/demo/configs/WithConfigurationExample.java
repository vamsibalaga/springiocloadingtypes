package com.example.demo.configs;

import com.example.demo.implementations.BoostMobile;
import com.example.demo.interfaces.ISim;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class WithConfigurationExample {

    @Bean
    @Primary
    public ISim configSim() {
        System.out.println("Creating BoostMobile - " + System.identityHashCode(this));
        System.out.println("WIT @Config - Creating BoostMobile Constructor..");
        return new BoostMobile();
    }

    @Bean
    public String serviceAWith() {
        System.out.println("serviceAWith..");
        ISim simInstance = configSim(); // Spring intercepts this call
        return "ServiceA-With uses: " + simInstance.hashCode();
    }

    @Bean
    public String serviceBWith() {
        System.out.println("serviceBWith..");
        ISim simInstance = configSim(); // Spring returns cached instance
        return "ServiceB-With uses: " + simInstance.hashCode();
    }
}
