package com.example.demo.implementations;

import com.example.demo.interfaces.ISim;

public class Verizon implements ISim {
    public Verizon(){
    System.out.println("Verizon Constructor....");
 }

    @Override
    public void calling() {
        System.out.println("Verizon Mobile Calling Enabled");
    }

    @Override
    public void data() {
        System.out.println("Verizon Mobile Data Enabled");
    }
}
