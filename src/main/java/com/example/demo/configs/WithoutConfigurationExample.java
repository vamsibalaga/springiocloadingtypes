package com.example.demo.configs;

import com.example.demo.implementations.BoostMobile;
import com.example.demo.interfaces.ISim;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


//@Import({WithoutConfigurationExample.class, WithConfigurationExample.class})
public class WithoutConfigurationExample {

    @Bean
    @Qualifier("withoutConfig")
    public ISim woConfigSim() {
        System.out.println("WITHOUT @Config - Creating BoostMobile Constructor..");
        return new BoostMobile();
    }

    @Bean
    public String serviceAWithout() {
        System.out.println("serviceAWithout..");
        ISim simInstance = woConfigSim(); // Direct method call - creates new instance
        return "ServiceA-Without uses: " + simInstance.hashCode();
    }

    @Bean
    public String serviceBWithout() {
        System.out.println("serviceBWithout..");
        ISim simInstance = woConfigSim(); // Another direct method call - creates new instance
        return "ServiceB-Without uses: " + simInstance.hashCode();
    }
}
