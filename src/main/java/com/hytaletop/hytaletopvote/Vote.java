package com.hytaletop.hytaletopvote;

import javax.annotation.Nonnull;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hytaletop.hytaletopvote.commands.ClaimCommand;
import com.hytaletop.hytaletopvote.config.ConfigManager;
import com.hytaletop.hytaletopvote.config.RewardConfig;

public class Vote extends JavaPlugin {

    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    ConfigManager configManager = new ConfigManager();

    public Vote(@Nonnull JavaPluginInit init) {
        super(init);
        LOGGER.atInfo().log("Initializable: ", this.getManifest().getName());

    }

    @Override
    protected void setup() {
        this.getCommandRegistry().registerCommand(new ClaimCommand(this));

    }

    @Override
    protected void shutdown() {
    }

    public RewardConfig getRewardConfig() {
        return this.configManager.getConfig();
    }

}