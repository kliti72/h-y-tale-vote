package com.hytaletop.hytaletopvote.commands;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hytaletop.hytaletopvote.TinyMsg;
import com.hytaletop.hytaletopvote.Vote;
import com.hytaletop.hytaletopvote.config.Config;
import com.hytaletop.hytaletopvote.model.ClaimResponse;
import com.hytaletop.hytaletopvote.utils.TimeUtils;;

@SuppressWarnings("null")
public class ClaimCommand extends AbstractPlayerCommand {

    Vote plugin;
    Config config;

    public ClaimCommand(Vote plugin, Config config) {
        super("claim", "claim reward.");
        this.plugin = plugin;
        this.config = config;
    }

    public void execute(
            @Nonnull CommandContext context, @Nonnull Store<EntityStore> store,
            @Nonnull Ref<EntityStore> ref,
            @Nonnull PlayerRef playerRef,
            @Nonnull World world) {

        if (!config.is_claim_enabled)
            return;

        String playerGameName = playerRef.getUsername();

        try {
            ClaimResponse res = ClaimResponse.fetch(plugin, config, playerGameName);

            String message = switch (res.status) {
                case ClaimResponse.NEVER_VOTED -> config.vote_not_found_message
                        .replace("{server_name}", "#" + res.serverName)
                        .replace("{server_link}", "h-y-tale-server.top/" + res.serverId)
                        .replace("{total_votes}", "" + res.voti_totali);

                case ClaimResponse.WAIT -> config.vote_time_to_wait_message
                        .replace("{time}", TimeUtils.formatTime(res.timeToWait));

                case ClaimResponse.SUCCESS -> config.disable_claim_message
                        ? null
                        : config.vote_claim_message;

                default -> null;
            };

            if (message != null)
                playerRef.sendMessage(TinyMsg.parse(message));

        } catch (Exception e) {
            plugin.getLogger().atInfo().log("[CLAIM] Errore API: " + e.getMessage());
        }
    }

}