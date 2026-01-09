package org.team8.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.*;
import java.net.URI;
import java.util.Base64;

public class AuthApi {
    private final HttpClient http = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    // ---------------------------------------------------------
    // LOGIN
    // ---------------------------------------------------------
    public LoginResponse login(String username, String password) throws Exception {
        var body = mapper.writeValueAsString(new LoginRequest(username, password));

        var req = HttpRequest.newBuilder(URI.create(Config.API_BASE + "/api/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        var resp = http.send(req, HttpResponse.BodyHandlers.ofString());
        System.out.println("[LOGIN] status=" + resp.statusCode() + " body=" + resp.body());

        if (resp.statusCode() == 200) {
            LoginResponse raw = mapper.readValue(resp.body(), LoginResponse.class);

            // Extract and normalize the REAL role from the JWT
            String mappedRole = extractRoleFromJwt(raw.token());
            System.out.println("[LOGIN] decoded role=" + mappedRole);

            // Return new LoginResponse with mapped role
            return new LoginResponse(raw.token(), mappedRole);

        } else if (resp.statusCode() == 401) {
            throw new IllegalArgumentException("Invalid username or password.");
        } else {
            throw new RuntimeException("HTTP " + resp.statusCode() + ": " + resp.body());
        }
    }

    // ---------------------------------------------------------
    // Extract actual role from JWT "groups" claim
    // ---------------------------------------------------------
    private String extractRoleFromJwt(String jwtToken) {
        try {
            // Split into header.payload.signature
            String[] parts = jwtToken.split("\\.");
            if (parts.length < 2) return "USER";

            // Decode payload
            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
            JsonNode payload = mapper.readTree(payloadJson);

            if (!payload.has("groups")) {
                return "USER";
            }

            for (JsonNode g : payload.get("groups")) {
                String group = g.asText().toLowerCase();

                if (group.contains("db_admin")) return "ADMIN";
                if (group.contains("db_manager")) return "MANAGER";
                if (group.contains("it_tech")) return "TECH";
                if (group.contains("manager")) return "MANAGER"; // your demo account
            }

            return "USER";
        } catch (Exception e) {
            e.printStackTrace();
            return "USER";
        }
    }

    // ---------------------------------------------------------
    // Login request/response records
    // ---------------------------------------------------------
    public record LoginRequest(String username, String password) {}
    public record LoginResponse(String token, String role) {}
}
