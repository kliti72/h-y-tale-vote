package com.pvpshield.spawntale;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.event.events.ecs.DropItemEvent;
import com.hypixel.hytale.server.core.event.events.ecs.InteractivelyPickupItemEvent;
import com.hypixel.hytale.server.core.event.events.entity.LivingEntityInventoryChangeEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer.ItemContainerChangeEvent;
import com.hypixel.hytale.server.core.inventory.transaction.Transaction;
import com.hypixel.hytale.server.core.io.adapter.PacketAdapters;
import com.hypixel.hytale.server.core.io.adapter.PacketFilter;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.pvpshield.spawntale.commands.MoveCameraCommand;
import com.pvpshield.spawntale.commands.OpenMenuCommand;
import com.pvpshield.spawntale.commands.OpenTutorialCommand;
import com.pvpshield.spawntale.commands.SetSpawnCommand;
import com.pvpshield.spawntale.commands.SpawnCommand;
import com.pvpshield.spawntale.config.ConfigManager;
import com.pvpshield.spawntale.config.ConfigSpawnTale;
import com.pvpshield.spawntale.event.CameraPresentation;
import com.pvpshield.spawntale.event.SetupLobbyHotBar;
import com.pvpshield.spawntale.event.TeleportSpawnOnJoin;
import com.pvpshield.spawntale.event.WelcomeNotify;
import com.pvpshield.spawntale.interaction.LifeShieldInteraction;

import javax.annotation.Nonnull;


public class SpawnTale extends JavaPlugin {

    ConfigSpawnTale configSpawnTale;
    ConfigManager configManager = new ConfigManager();
    private PacketFilter inboundFilter;

    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public SpawnTale(@Nonnull JavaPluginInit init) {
        super(init);
        LOGGER.atInfo().log("Initializable: ", this.getManifest().getName());
    }

    @Override
    protected void setup() {
        this.configSpawnTale = this.configManager.getConfig(); // create json or read if exist
        this.getCommandRegistry().registerCommand(new SpawnCommand(this));
    }

    @Override
    protected void shutdown() {
        this.configManager.save();
        LOGGER.atInfo().log("Deactive", this.getManifest().getName());
    }

    public ConfigSpawnTale getConfig() {
        return this.configSpawnTale;
    }

    public ConfigManager getConfigManager() {
        return this.configManager;
    }
}