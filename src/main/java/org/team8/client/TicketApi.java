package org.team8.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class TicketApi {

    private final HttpClient http = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    private HttpRequest.Builder authorized(URI uri) {
        return HttpRequest.newBuilder(uri)
                .header("Authorization", "Bearer " + Session.token());
    }

    // ---------------------------------------------------------------------
    // LIST ALL TICKETS (used by Dashboard)
    // ---------------------------------------------------------------------
    public List<TicketDto> listAll() throws IOException, InterruptedException {
        HttpRequest req = authorized(URI.create(Config.API_BASE + "/api/tickets"))
                .GET()
                .build();

        HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() == 200) {
            return mapper.readValue(resp.body(), new TypeReference<List<TicketDto>>() {});
        }
        if (resp.statusCode() == 401) {
            throw new IllegalStateException("Not authorized. Please log in again.");
        }
        if (resp.statusCode() == 403) {
            throw new IllegalStateException("Forbidden for role: " + Session.role());
        }
        throw new RuntimeException("GET /api/tickets failed: HTTP " + resp.statusCode());
    }

    // ---------------------------------------------------------------------
    // CREATE MULTIPLE ONBOARDING TICKETS FROM ONE FORM SUBMISSION
    // ---------------------------------------------------------------------
    public List<TicketDto> createOnboardingBatch(OnboardingTicketBatchRequest payload)
            throws IOException, InterruptedException {

        String json = mapper.writeValueAsString(payload);

        HttpRequest req = authorized(URI.create(Config.API_BASE + "/api/tickets/onboarding/batch"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString());
        System.out.println("[POST /onboarding/batch] status=" + resp.statusCode()
                + " body=" + resp.body());

        if (resp.statusCode() == 200 || resp.statusCode() == 201) {
            return mapper.readValue(resp.body(), new TypeReference<List<TicketDto>>() {});
        }
        if (resp.statusCode() == 400) {
            throw new IllegalArgumentException("Validation error: " + resp.body());
        }
        if (resp.statusCode() == 401) {
            throw new IllegalStateException("Not authorized. Please log in again.");
        }
        if (resp.statusCode() == 403) {
            throw new IllegalStateException("Forbidden for role: " + Session.role());
        }
        throw new RuntimeException(
                "POST /api/tickets/onboarding/batch failed: HTTP "
                        + resp.statusCode() + " " + resp.body());
    }

    // ---------------------------------------------------------------------
    // APPROVE A TICKET (ADMIN/MANAGER)
    // ---------------------------------------------------------------------
    public TicketDto approveTicket(long id) throws Exception {
        String tok = Session.token();
        if (tok == null || tok.isBlank()) {
            throw new IllegalStateException("Not authorized. Please log in again.");
        }

        var req = HttpRequest.newBuilder(URI.create(Config.API_BASE + "/api/tickets/" + id + "/approve"))
                .header("Authorization", "Bearer " + tok)
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build();

        var resp = http.send(req, HttpResponse.BodyHandlers.ofString());
        System.out.println("[PUT] approve status=" + resp.statusCode() + " body=" + resp.body());

        if (resp.statusCode() == 401) throw new IllegalStateException("Not authorized. Please log in again.");
        if (resp.statusCode() == 403) throw new IllegalStateException("Forbidden. Only managers/admins can approve.");
        if (resp.statusCode() < 200 || resp.statusCode() >= 300) throw new IllegalStateException(resp.body());

        return mapper.readValue(resp.body(), TicketDto.class);
    }

    // ---------------------------------------------------------------------
    // CREATE RETIREMENT TICKET
    // ---------------------------------------------------------------------
    public TicketDto createRetirement(TicketDto payload)
            throws IOException, InterruptedException {

        String json = mapper.writeValueAsString(payload);
        System.out.println("[POST /api/tickets/retirement] payload=" + json);

        HttpRequest req = authorized(URI.create(Config.API_BASE + "/api/tickets/retirement"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString());
        System.out.println("[POST /retirement] status=" + resp.statusCode() + " body=" + resp.body());

        if (resp.statusCode() == 201) {
            return mapper.readValue(resp.body(), TicketDto.class);
        }
        if (resp.statusCode() == 400) {
            throw new IllegalArgumentException("Validation error: " + resp.body());
        }
        if (resp.statusCode() == 401) {
            throw new IllegalStateException("Not authorized. Please log in again.");
        }
        if (resp.statusCode() == 403) {
            throw new IllegalStateException("Forbidden for role: " + Session.role());
        }

        throw new RuntimeException("POST /api/tickets/retirement failed: HTTP "
                + resp.statusCode() + " " + resp.body());
    }
}
