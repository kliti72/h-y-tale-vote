package com.hytaletop.hytaletopvote.core;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.hytaletop.hytaletopvote.Vote;
import com.hytaletop.hytaletopvote.config.Config;
import com.hytaletop.hytaletopvote.model.StatusPingResponse;
import com.hytaletop.hytaletopvote.model.StatusPingSecondaryResponse;
import com.hytaletop.hytaletopvote.utils.StatusUtils;

public class RegisterStatusService {

    private final Vote plugin;
    private final Config config;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    public RegisterStatusService(Vote plugin, Config config) {
        this.plugin = plugin;
        this.config = config;
    }

   public void registerAll() {
        if (config.is_principal_network) {
            registerPrimaryPing();
        } else {
            registerSecondaryPing();
        }
    }

    // ── Ping principale ogni 2 minuti ─────────────────────────────────────────
    private void registerPrimaryPing() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                StatusPingResponse res = StatusPingResponse.fetch(
                    plugin, config,
                    StatusUtils.getPlayersOnline(),
                    StatusUtils.getPlayersMax(),
                    true,
                    StatusUtils.getLatencyMs()
                );
                Vote.LOGGER.atInfo().log("[PING] Status: " + res.status + " | success: " + res.success);
            } catch (Exception e) {
                Vote.LOGGER.atInfo().log("[PING] Errore: " + e.getMessage());
            }
        }, 0, 1, TimeUnit.MINUTES);
    }

    // ── Ping secondario ogni 2 minuti ─────────────────────────────────────────
    private void registerSecondaryPing() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                StatusPingSecondaryResponse res = StatusPingSecondaryResponse.fetch(
                    plugin, config,
                    config.secondary_id,
                    StatusUtils.getPlayersOnline()
                );
                Vote.LOGGER.atInfo().log("[PING-SECONDARY] Status: " + res.status + " | success: " + res.success);
            } catch (Exception e) {
                Vote.LOGGER.atInfo().log("[PING-SECONDARY] Errore: " + e.getMessage());
            }
        }, 0, 1, TimeUnit.MINUTES);
    }

    // ── Shutdown pulito quando il plugin si spegne ────────────────────────────
    public void shutdown() {
        scheduler.shutdown();
        Vote.LOGGER.atInfo().log("[INTERVAL] Scheduler fermato.");
    }
}