package com.example.shiny_potato.dto;

public class UserDTO {
    private Long id;
    private String name;
    private String password;
    private String email;
    private String userType;

    public UserDTO() { }

    public UserDTO(Long id, String name, String password, String email, String userType) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.userType = userType;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getUserType() {
        return userType;
    }
}
