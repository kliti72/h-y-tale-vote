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
import com.hytaletop.hytaletopvote.utils.ResolvePlayerUtils;

@SuppressWarnings("null")
public class ClaimTest extends AbstractPlayerCommand {

    Vote plugin;
    Config config;

    public ClaimTest(Vote plugin, Config config) {
        super("claimtest", "claim reward.");
        this.plugin = plugin;
        this.config = config;

        requirePermission("hyvote.claim.test");
    }

    public void execute(
            @Nonnull CommandContext context, @Nonnull Store<EntityStore> store,
            @Nonnull Ref<EntityStore> ref,
            @Nonnull PlayerRef playerRef,
            @Nonnull World world) {

        giveReward(context, store, ref, playerRef, world);

    }

    public void giveReward(
            @Nonnull CommandContext context, @Nonnull Store<EntityStore> store,
            @Nonnull Ref<EntityStore> ref,
            @Nonnull PlayerRef playerRef,
            @Nonnull World world) {
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

            rewardWindow.registerCloseEvent(event -> {
                rewardContainer.forEach((slot, item) -> {
                    if (item != null) {
                        player.giveItem(item, ref, store);
                    }
                });
            });

            player.getPageManager().setPageWithWindows(ref, store, Page.Inventory, true, rewardWindow);

        } else {
            playerRef.sendMessage(TinyMsg.parse(config.fullInventory));
        }
    }
}