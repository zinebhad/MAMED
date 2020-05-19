package com.example.pfe.Models;

public class Adherent extends User{
       private  String name;
       private String CIN;

    public Adherent(String email, String password, String role, String name, String CIN) {
        super(email, password, role);
        this.name = name;
        this.CIN = CIN;
    }
    public Adherent(String email, String name,String CIN) {
        super(email);
        this.name=name;
        this.CIN = CIN;
    }


}
