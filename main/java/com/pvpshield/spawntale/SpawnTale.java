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
        this.getCommandRegistry().registerCommand(new SetSpawnCommand(this));
        this.getCommandRegistry().registerCommand(new MoveCameraCommand());
        this.getCommandRegistry().registerCommand(new OpenTutorialCommand());
        this.getCommandRegistry().registerCommand(new OpenMenuCommand());
        WelcomeNotify welcome = new WelcomeNotify();
        TeleportSpawnOnJoin teleportSpawnOnJoin = new TeleportSpawnOnJoin(this);

        this.getEventRegistry().registerGlobal(PlayerConnectEvent.class, welcome::onPlayerConnect);
        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, welcome::onPlayerReady);
        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, teleportSpawnOnJoin::teleportPlayerOnJoin);


        // Clear hot bar and set navigator
        SetupLobbyHotBar lobbyHotBar = new SetupLobbyHotBar(this);
        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, lobbyHotBar::setupLobbyHotBar);


        CameraPresentation cameraPresentation = new CameraPresentation(this);
        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, cameraPresentation::cameraPresentation);
        this.configManager.save();
        LOGGER.atInfo().log("Setuped: ", this.getManifest().getName());

        // Client Packet to manage hotbar handle
        // inboundFilter = PacketAdapters.registerInbound(new AbilitySlotHandler(this));
        // Replace this for custom iteraction
        
        this.getCodecRegistry(Interaction.CODEC).register("shield_menu_interaction", LifeShieldInteraction.class, LifeShieldInteraction.CODEC);

    }

    public void relaodPlugin() {
        this.configManager.save();
        this.shutdown();
        this.setup();
    }

    @Override
    protected void shutdown() {
        this.configManager.save();
        LOGGER.atInfo().log("Deactive", this.getManifest().getName());

        // Elimina i filtri in ascolto del packet
        if (inboundFilter != null) {
            PacketAdapters.deregisterInbound(inboundFilter);
        }
        
    }

    public ConfigSpawnTale getConfig() {
        return this.configSpawnTale;
    }

    public ConfigManager getConfigManager() {
        return this.configManager;
    }
}