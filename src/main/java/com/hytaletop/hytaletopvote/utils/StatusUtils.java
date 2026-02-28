package com.hytaletop.hytaletopvote.utils;

import com.hypixel.hytale.server.core.HytaleServerConfig;
import com.hytaletop.hytaletopvote.listener.PlayerCountListener;

public class StatusUtils {

    static public int getPlayersOnline() {
        return PlayerCountListener.getOnlinePlayers();
    }

    static public int getPlayersMax() {
        return HytaleServerConfig.load().getMaxPlayers();
    }

    static public int getLatencyMs() {
        // non esiste una latenza globale del server, 0 è il valore più sensato
        return 0;
    }
}