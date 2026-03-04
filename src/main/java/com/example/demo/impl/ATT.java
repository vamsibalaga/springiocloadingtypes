package com.example.demo.implementations;

import com.example.demo.interfaces.ISim;

public class ATT implements ISim {

    public  ATT(){
        System.out.println("ATT Constructor....");
    }

    @Override
    public void calling() {
        System.out.println("ATT Mobile Calling Enabled");
    }

    @Override
    public void data() {
        System.out.println("ATT Mobile Calling Enabled");

    }
}
