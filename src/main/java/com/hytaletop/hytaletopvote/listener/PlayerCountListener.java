package com.hytaletop.hytaletopvote.listener;

import java.util.concurrent.atomic.AtomicInteger;

import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;

public class PlayerCountListener {

    private static final AtomicInteger onlinePlayers = new AtomicInteger(0);

    public void onPlayerReady(PlayerReadyEvent event) {
        onlinePlayers.incrementAndGet();
    }

    public void onPlayerLeave(PlayerDisconnectEvent event) {
        onlinePlayers.decrementAndGet();
    }

    public static int getOnlinePlayers() {
        return onlinePlayers.get();
    }

}