package com.hytaletop.hytaletopvote.commands;

import javax.annotation.Nonnull;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.Page;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.windows.ContainerWindow;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.SimpleItemContainer;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hytaletop.hytaletopvote.TinyMsg;
import com.hytaletop.hytaletopvote.Vote;
import com.hytaletop.hytaletopvote.config.Config;
import com.hytaletop.hytaletopvote.config.ConfigManager;
import com.hytaletop.hytaletopvote.utils.ResolvePlayerUtils;

@SuppressWarnings("null")
public class RewardCommand extends AbstractPlayerCommand {

    public Vote plugin;
    public Config config;
    private final ConfigManager configManager;

    public RewardCommand(Vote plugin, Config config, ConfigManager configManager) {
        super("setreward", "claim reward.");
        this.plugin = plugin;
        this.config = config;
        this.configManager = configManager;

        requirePermission("hyvote.set.reward");

    }

    public void execute(
            @Nonnull CommandContext context, @Nonnull Store<EntityStore> store,
            @Nonnull Ref<EntityStore> ref,
            @Nonnull PlayerRef playerRef,
            @Nonnull World world) {
        Player player = ResolvePlayerUtils.getPlayer(ref, store);

        SimpleItemContainer rewardContainer = new SimpleItemContainer((short) 27);
        // MenuUI page = new MenuUI(playerRef);

        for(ItemStack item : config.getStacks()) {
                rewardContainer.addItemStack(item);
        }

        ContainerWindow rewardWindow = new ContainerWindow(rewardContainer);

        rewardWindow.registerCloseEvent(event -> {
            this.config.clearRewards();
            rewardContainer.forEach((slot, item) -> {
                if (item != null) {
                    this.config.addReward(new ItemStack(item.getItemId(), item.getQuantity()));
                }
            });
            playerRef.sendMessage(TinyMsg.parse(config.rewardUpdateMessage));
            this.configManager.save();
        });

        player.getPageManager().setPageWithWindows(ref, store, Page.Inventory, true, rewardWindow);
    }

}
