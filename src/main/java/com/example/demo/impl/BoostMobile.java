package com.example.demo.implementations;

import com.example.demo.interfaces.ISim;

public class BoostMobile implements ISim {

    public BoostMobile(){
        System.out.println("BoostMobile Constructor....");
    }

    @Override
    public int hashCode() {
        System.out.println("BoostMobile.hashCode()"+super.hashCode());
        return super.hashCode();
    }

    @Override
    public void calling() {
        System.out.println("Boost Mobile Calling Enabled");
    }

    @Override
    public void data() {
        System.out.println("Boost Mobile Data Enabled");
    }
}
