package com.hytaletop.hytaletopvote;

import javax.annotation.Nonnull;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hytaletop.hytaletopvote.commands.ClaimCommand;

public class Vote extends JavaPlugin {

    public Vote(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        this.getCommandRegistry().registerCommand(new ClaimCommand(this));
    }

    @Override
    protected void shutdown() {
    }

}