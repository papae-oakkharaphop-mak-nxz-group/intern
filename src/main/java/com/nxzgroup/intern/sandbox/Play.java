package com.nxzgroup.intern.sandbox;

import java.io.Console;

public class Play {
    static void display(String... values){
        System.out.println("new invoke");
        for(String test:values){
            System.out.println(test);
        }
    }
    public static void main(String args[]){
        display();
        display("arg1","arg2","args3","args" +
                "4");

    }
}
