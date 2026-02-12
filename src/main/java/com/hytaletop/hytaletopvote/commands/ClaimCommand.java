package com.hytaletop.hytaletopvote.commands;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import javax.annotation.Nonnull;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hytaletop.hytaletopvote.TinyMsg;
import com.hytaletop.hytaletopvote.Vote;


import java.time.Duration;

@SuppressWarnings("null")
public class ClaimCommand extends AbstractPlayerCommand  {
    
    Vote plugin;

    public ClaimCommand(Vote plugin) {
        super("claim", "claim reward.");
        this.plugin = plugin;
    }


    public void execute(
        @Nonnull CommandContext context, @Nonnull Store<EntityStore> store, 
        @Nonnull Ref<EntityStore> ref, 
        @Nonnull PlayerRef playerRef, 
        @Nonnull World world) {

        String apiUrl = "http://localhost:3000/voted/" + playerRef.getUsername();

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .timeout(Duration.ofSeconds(10))
                .header("Accept", "application/json")
                .GET()
                .build();

        try {
            // Esegue la chiamata in modo sincrono
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            // Controlliamo lo status code
            int statusCode = response.statusCode();
            System.out.println("Status: " + statusCode);

            if (statusCode >= 200 && statusCode < 300) {
                String jsonResponse = response.body();
                playerRef.sendMessage(TinyMsg.parse(jsonResponse.toString()));
            } else {
                System.out.println("Errore nella chiamata: " + statusCode);
                System.out.println("Body: " + response.body());
            }

        } catch (Exception e) {
            System.err.println("Errore durante la chiamata API:");
            e.printStackTrace();
        }
    }
}
