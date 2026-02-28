package com.hytaletop.hytaletopvote;

import javax.annotation.Nonnull;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hytaletop.hytaletopvote.commands.ClaimCommand;
import com.hytaletop.hytaletopvote.config.ConfigManager;
import com.hytaletop.hytaletopvote.core.RegisterStatusService;
import com.hytaletop.hytaletopvote.listener.PlayerCountListener;
import com.hytaletop.hytaletopvote.utils.CodeUtils;
import com.hytaletop.hytaletopvote.utils.StatusUtils;
import com.hytaletop.hytaletopvote.config.Config;

public class Vote extends JavaPlugin {
    public static final String BASE_URL = "https://h-y-tale-server.top/api";
    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    
    ConfigManager configManager = new ConfigManager();
    RegisterStatusService intervals = new RegisterStatusService(this, this.configManager.getConfig());
    PlayerCountListener playerCounter = new PlayerCountListener();

    public Vote(@Nonnull JavaPluginInit init) {
        super(init);
        LOGGER.atInfo().log("Initializable: ", this.getManifest().getName());

    }

    @Override
    protected void setup() {
        intervals.registerAll();
        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, playerCounter::onPlayerReady);
        this.getEventRegistry().registerGlobal(PlayerDisconnectEvent.class, playerCounter::onPlayerLeave);
        this.getCommandRegistry().registerCommand(new ClaimCommand(this, this.configManager.getConfig()));
    }

    @Override
    protected void shutdown() {
        intervals.shutdown();
    }

    public Config getRewardConfig() {
        
        if(this.configManager.getConfig().secondary_id.equals("")) {
            this.configManager.getConfig().secondary_id = CodeUtils.genUniqueCode(); 
        }

        return this.configManager.getConfig();
    }


}