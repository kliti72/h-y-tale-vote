package com.pvpshield.spawntale.commands;

import javax.annotation.Nonnull;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import com.pvpshield.spawntale.SpawnTale;
import com.pvpshield.spawntale.config.ConfigSpawnTale;
import com.pvpshield.spawntale.config.TinyMsg;


@SuppressWarnings("null")
public class SpawnCommand extends AbstractPlayerCommand  {
    
    SpawnTale spawnTale;

    public SpawnCommand(SpawnTale spawnTale) {
        super("shieldspawn", "go to the spawn.");
        this.spawnTale = spawnTale;

    }

    public ConfigSpawnTale getConfig() {
        return this.spawnTale.getConfig();
    };


    public void execute(@Nonnull CommandContext context, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {

        String apiUrl = "https://jsonplaceholder.typicode.com/users/1";

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .timeout(Duration.ofSeconds(10))
                .header("Accept", "application/json")
                .GET()
                .build();

    }
}
