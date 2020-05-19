package com.example.pfe.Models;

public class Admin extends User {
    public Admin(String email) {
        super(email);
    }

    public Admin(String email, String password, String role) {
        super(email, password, role);
    }
}
