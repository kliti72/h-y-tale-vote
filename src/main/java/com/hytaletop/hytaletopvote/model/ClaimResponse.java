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

public class ClaimResponse {

    // ── Costanti status ───────────────────────────────────────────────────────
    public static final int SECRET_ERROR = -1;
    public static final int NEVER_VOTED  = 0;
    public static final int WAIT         = 1;
    public static final int SUCCESS      = 2;

    // ── Campi response ────────────────────────────────────────────────────────
    public final boolean success;
    public final int     status;
    public final int     serverId;
    public final int     timeToWait; // minuti, presente solo su status 1
    public final String     serverName; // minuti, presente solo su status 1
    public final int     voti_totali; // minuti, presente solo su status 1
    
    // ── Costruttore privato — si usa solo ClaimResponse.fetch() ──────────────
    private ClaimResponse(boolean success, int status, int serverId, int timeToWait, String serverName, int voti_totali) {
        this.success    = success;
        this.status     = status;
        this.serverId   = serverId;
        this.timeToWait = timeToWait;
        this.serverName = serverName;
        this.voti_totali = voti_totali;
    }

    // ── Metodo principale — chiama l'API e ritorna ClaimResponse ─────────────
    public static ClaimResponse fetch(Vote plugin, Config config, String playerGameName) throws Exception {
        String url = "%s/vote/claim/%s/%s".formatted(Vote.BASE_URL, config.secret_key, playerGameName);
        plugin.getLogger().atInfo().log("E' stato mandato la richiesta:" + url);
        
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        String body = client.send(request, BodyHandlers.ofString()).body();
        Vote.LOGGER.atInfo().log("[CLAIM] Response: " + body);

        JsonObject json = JsonParser.parseString(body).getAsJsonObject();

        return new ClaimResponse(
            json.has("success")    && json.get("success").getAsBoolean(),
            json.has("status")     ? json.get("status").getAsInt()     : -1,
            json.has("serverId")   ? json.get("serverId").getAsInt()   : 0,
            json.has("time_to_wait") ? json.get("time_to_wait").getAsInt() : 0,
            json.has("serverName") ? json.get("serverName").getAsString() : "undefined",
            json.has("voti_totali") ? json.get("voti_totali").getAsInt() : 0
        );
    }
}