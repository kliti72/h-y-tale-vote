package com.hytaletop.hytaletopvote.utils;

import java.util.UUID;

import javax.annotation.Nonnull;

import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer.ItemContainerChangeEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

@SuppressWarnings("null")
public class ResolvePlayerUtils {
    

    public final static Player getPlayerFromContext(CommandContext commandContext) {
        return commandContext.senderAs(Player.class);
    }

    public final static PlayerRef getPlayerRefFromContext(@Nonnull CommandContext commandContext) {
        @Nonnull UUID PlayerUUID = commandContext.sender().getUuid();
        return Universe.get().getPlayer(PlayerUUID);
    }


    public final static PlayerRef getPlayerRefFromPlayer(Player player) {
        Ref<EntityStore> ref = player.getReference();
        Store<EntityStore> store = ref.getStore();
        return store.getComponent(ref, PlayerRef.getComponentType());
    }

    public static Player getPlayerFromEvent(PlayerReadyEvent event) {
        return event.getPlayer();

    }

    // WAIT
    public static ItemContainer getPlayerFromEvent(ItemContainerChangeEvent event) {
        return event.container();
    }

    public static Store<EntityStore> getStoreByEvent(PlayerReadyEvent event) {
        Player player = ResolvePlayerUtils.getPlayerFromEvent(event);
        Ref<EntityStore> ref = player.getReference();
        return ref.getStore();
    }
    @Nonnull
    public static Ref<EntityStore> getRefByEvent(PlayerReadyEvent event) {
        Player player = ResolvePlayerUtils.getPlayerFromEvent(event);
        return player.getReference();
    }

    public static Player getPlayer(Ref<EntityStore> ref, Store<EntityStore> store) {
         return store.getComponent(ref, Player.getComponentType());
    }

    public static Ref<EntityStore> getReferencesByRef(PlayerRef playerRef) {
        return playerRef.getReference();
    }
    
    public static Store<EntityStore> getStoreByRef(PlayerRef playerRef) {
        return getReferencesByRef(playerRef).getStore();
    }

    public static World getWorldByEvent(PlayerReadyEvent event) {
        Player player = getPlayerFromEvent(event);
        PlayerRef playerRef = getPlayerRefFromPlayer(player);
        return Universe.get().getWorld(playerRef.getWorldUuid());
    }

    public static World getWorldByStore(Store<EntityStore> store) {
        return store.getExternalData().getWorld();
    }

    public static PlayerRef getPlayerRefFromEvent(PlayerReadyEvent event) {
        Ref<EntityStore> ref = event.getPlayerRef();
        Store<EntityStore> store = ref.getStore();
        return store.getComponent(ref, PlayerRef.getComponentType());
    }

    public static PlayerRef getPlayerRefFromInteractionContext(InteractionContext ctx) {
        Ref<EntityStore> ref = ctx.getEntity();
        Store<EntityStore> store = ref.getStore();
        Player player = getPlayer(ref, store);
        return getPlayerRefFromPlayer(player);
    } 

    public static Store<EntityStore> getStoreFromInteractionContext(InteractionContext ctx) {
        CommandBuffer<EntityStore> commandBuffer = ctx.getCommandBuffer();
        return commandBuffer.getExternalData().getStore();
    }

    public static Player getPlayerFromInteractionContext(InteractionContext ctx) {
        CommandBuffer<EntityStore> commandBuffer = ctx.getCommandBuffer();
        Ref<EntityStore> ref = ctx.getEntity();
        return commandBuffer.getComponent(ref, Player.getComponentType());
    }

    public static Ref<EntityStore> getRefFromInteractionContext(InteractionContext ctx) {
        return ctx.getEntity();
    }

    public static World getWorldByPlayerRef(PlayerRef playerRef) {
        return Universe.get().getWorld(playerRef.getWorldUuid());
    }

}
