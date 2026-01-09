package org.team8.demo;

/*
import java.net.http.*;
import java.net.URI;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class ApiClient {

    private static final String BASE_URL = "http://localhost:8080";
    private final HttpClient http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(3))
            .build();
    private final ObjectMapper om = new ObjectMapper();
    private String jwt; // store in memory

    public static class LoginRequest { public String username; public String password; }
    public static class LoginResponse { public String token; public String role; public String username; }

    public boolean login(String username, String password) throws Exception {
        LoginRequest req = new LoginRequest();
        req.username = username;
        req.password = password;

        String body = om.writeValueAsString(req);
        HttpRequest httpReq = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> resp = http.send(httpReq, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() == 200) {
            LoginResponse lr = om.readValue(resp.body(), LoginResponse.class);
            this.jwt = lr.token;
            return true;
        } else {
            System.err.println("Login failed: status=" + resp.statusCode() + ", body=" + resp.body());
            return false;
        }
    }

    // Example of an authenticated GET
    public String getProtected(String path) throws Exception {
        HttpRequest.Builder b = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .GET();
        if (jwt != null) b.header("Authorization", "Bearer " + jwt);
        HttpResponse<String> resp = http.send(b.build(), HttpResponse.BodyHandlers.ofString());
        return resp.statusCode() + ":" + resp.body();
    }

    public String get(String path) throws Exception {
        HttpRequest.Builder b = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .GET();
        if (jwt != null) b.header("Authorization", "Bearer " + jwt);
        HttpResponse<String> resp = http.send(b.build(), HttpResponse.BodyHandlers.ofString());
        return resp.body();
    }

    public String ping() throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/ping"))
                .GET()
                .build();
        HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString());
        return resp.statusCode() + ":" + resp.body(); // e.g., "200:pong"
    }
}

 */
