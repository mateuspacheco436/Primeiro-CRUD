package com.example.crud.Domain.User;

public enum UserRoles {

    Admin("ADMIN"),
    User("USER");

    private String role;

    UserRoles(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
