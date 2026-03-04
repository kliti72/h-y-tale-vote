package com.hytaletop.hytaletopvote.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.Page;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.windows.ContainerWindow;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import com.hypixel.hytale.server.core.inventory.container.SimpleItemContainer;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hytaletop.hytaletopvote.TinyMsg;
import com.hytaletop.hytaletopvote.Vote;
import com.hytaletop.hytaletopvote.config.Config;
import com.hytaletop.hytaletopvote.model.ClaimResponse;
import com.hytaletop.hytaletopvote.utils.ResolvePlayerUtils;
import com.hytaletop.hytaletopvote.utils.TimeUtils;;

@SuppressWarnings("null")
public class ClaimCommand extends AbstractPlayerCommand {

    Vote plugin;
    Config config;

    public ClaimCommand(Vote plugin, Config config) {
        super("claim", "claim reward.");
        this.plugin = plugin;
        this.config = config;

        addAliases("vote", "v", "votes", "rewaord", "link");
    }

    public void execute(
            @Nonnull CommandContext context, @Nonnull Store<EntityStore> store,
            @Nonnull Ref<EntityStore> ref,
            @Nonnull PlayerRef playerRef,
            @Nonnull World world) {

        if (!config.isClaimEnabled)
            return;

        String playerGameName = playerRef.getUsername().toLowerCase();

        try {
            ClaimResponse res = ClaimResponse.fetch(plugin, config, playerGameName);

            String message = switch (res.status) {
                case ClaimResponse.SECRET_ERROR -> config.errorSecretNotValidMessage;
                case ClaimResponse.NEVER_VOTED -> config.voteNotFoundMessage
                        .replace("{server_name}", "" + res.serverName)
                        .replace("{server_link}", "https://h-y-tale-server.top/server/" + res.serverId)
                        .replace("{total_votes}", "" + res.voti_totali)
                        .replace("{player_name}", "" + playerGameName);

                case ClaimResponse.WAIT -> config.voteTimeToWaitMessage
                        .replace("{time}", TimeUtils.formatTime(res.timeToWait))
                        .replace("{player_name}", "" + playerGameName);

                case ClaimResponse.SUCCESS -> {
                    giveReward(context, store, ref, playerRef, world);
                    yield config.voteClaimMessage;
                }

                default -> null;
            };

            if (message != null)
                playerRef.sendMessage(TinyMsg.parse(message));

        } catch (Exception e) {
            plugin.getLogger().atInfo().log("[CLAIM] Errore API: " + e.getMessage());
        }
    }

    public void giveReward(@Nonnull CommandContext context, @Nonnull Store<EntityStore> store,
            @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {

        Player player = ResolvePlayerUtils.getPlayer(ref, store);

        SimpleItemContainer rewardContainer = new SimpleItemContainer((short) 27);
        Inventory inv = player.getInventory();

        ItemContainer ic = inv.getStorage();
        List<ItemStack> list = new ArrayList<>(Arrays.asList(config.getStacks()));
        if (ic.canAddItemStacks(list)) {
            playerRef.sendMessage(TinyMsg.parse("Hai ricevuto i tuoi premi"));

            for (ItemStack item : config.getStacks()) {
                rewardContainer.addItemStack(item);
            }

            ContainerWindow rewardWindow = new ContainerWindow(rewardContainer);

            // Claim Menu
            if (config.openMenuOnClaim) {
                rewardWindow.registerCloseEvent(event -> {
                    rewardContainer.forEach((slot, item) -> {
                        if (item != null) {
                            player.giveItem(item, ref, store);
                        }
                    });
                });

                player.getPageManager().setPageWithWindows(ref, store, Page.Inventory, true, rewardWindow);
            } else {
                rewardContainer.forEach((slot, item) -> {
                    if (item != null) {
                        player.giveItem(item, ref, store);
                    }
                });
            }

        } else {
            playerRef.sendMessage(TinyMsg.parse(config.fullInventory));
        }
    }
}