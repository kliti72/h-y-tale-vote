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

public class StatusPingSecondaryResponse {

    // ── Costanti status ───────────────────────────────────────────────────────
    public static final int SECRET_ERROR = -1;
    public static final int SUCCESS      = 0;

    // ── Campi response ────────────────────────────────────────────────────────
    public final boolean success;
    public final int     status;

    // ── Costruttore privato — si usa solo StatusPingSecondaryResponse.fetch() ─
    private StatusPingSecondaryResponse(boolean success, int status) {
        this.success = success;
        this.status  = status;
    }

    // ── Metodo principale — chiama l'API e ritorna StatusPingSecondaryResponse ─
    public static StatusPingSecondaryResponse fetch(Vote plugin, Config config, String secondaryId, int playersOnline) throws Exception {
        String url = Vote.BASE_URL + "/servers/status/ping/secondary";
        plugin.getLogger().atInfo().log("[PING-SECONDARY] Mandando status ping secondario: " + url);

        JsonObject bodyJson = new JsonObject();
        bodyJson.addProperty("secret_key",      config.secret_key);
        bodyJson.addProperty("secondary_id",    secondaryId);
        bodyJson.addProperty("players_online",  playersOnline);

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .header("Accept",       "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(bodyJson.toString()))
                .build();

        String responseBody = client.send(request, BodyHandlers.ofString()).body();
        Vote.LOGGER.atInfo().log("[PING-SECONDARY] Response: " + responseBody);

        JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();

        return new StatusPingSecondaryResponse(
            json.has("success") && json.get("success").getAsBoolean(),
            json.has("status")  ? json.get("status").getAsInt() : -1
        );
    }
}