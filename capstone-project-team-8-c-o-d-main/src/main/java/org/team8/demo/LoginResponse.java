package org.team8.demo;

public record LoginResponse(String token, String role) {}

/*
public class LoginResponse {

    public String token;
    public String role;
    public String username;
    public LoginResponse(String token, String role, String username) {
        this.token = token; this.role = role; this.username = username;
    }
}

 */
