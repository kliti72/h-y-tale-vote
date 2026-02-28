package com.hytaletop.hytaletopvote.model;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hytaletop.hytaletopvote.Vote;
import com.hytaletop.hytaletopvote.config.Config;

public class StatusPingResponse {

    // ── Costanti status ───────────────────────────────────────────────────────
    public static final int SECRET_ERROR = -1;
    public static final int SUCCESS      = 0;

    // ── Campi response ────────────────────────────────────────────────────────
    public final boolean success;
    public final int     status;

    // ── Costruttore privato — si usa solo StatusPingResponse.fetch() ─────────
    private StatusPingResponse(boolean success, int status) {
        this.success = success;
        this.status  = status;
    }

    // ── Metodo principale — chiama l'API e ritorna StatusPingResponse ─────────
    public static StatusPingResponse fetch(Vote plugin, Config config, int playersOnline, int playersMax, boolean isOnline, int latencyMs) throws Exception {
        String url = Vote.BASE_URL + "/servers/status/ping";
        plugin.getLogger().atInfo().log("[PING] Mandando status ping: " + url);

        JsonObject bodyJson = new JsonObject();
        bodyJson.addProperty("secret_key",     config.secret_key);
        bodyJson.addProperty("players_online", playersOnline);
        bodyJson.addProperty("players_max",    playersMax);
        bodyJson.addProperty("is_online",      isOnline);
        bodyJson.addProperty("latency_ms",     latencyMs);

        String body = bodyJson.toString();


        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .header("Accept",       "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        String responseBody = client.send(request, BodyHandlers.ofString()).body();
        Vote.LOGGER.atInfo().log("[PING] Response: " + responseBody);

        JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();

        return new StatusPingResponse(
            json.has("success") && json.get("success").getAsBoolean(),
            json.has("status")  ? json.get("status").getAsInt() : -1
        );
    }
}