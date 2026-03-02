package com.hytaletop.hytaletopvote.UI;


import javax.annotation.Nonnull;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.protocol.packets.interface_.Page;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hytaletop.hytaletopvote.TinyMsg;

@SuppressWarnings("Null")
public class MenuUI extends InteractiveCustomUIPage<UIEventBind> {
    
    public MenuUI(@Nonnull PlayerRef playerRef) {
        super(playerRef, CustomPageLifetime.CanDismiss, UIEventBind.CODEC);
    }

    @Override
    public void build(@Nonnull Ref<EntityStore> ref, @Nonnull UICommandBuilder cmd, @Nonnull UIEventBuilder evt,
        @Nonnull Store<EntityStore> store) {
        cmd.append("Hud/UIMenu.ui");
        cmd.set("#Menutitle.TextSpans", TinyMsg.parse("<gradient:yellow:green><b> PVPSHIELD </b></gradient>"));
        cmd.set("#Subtitle.TextSpans", TinyMsg.parse("<orange:yellow:green><b> Seleziona la modalità.</b></gradient>"));
        cmd.set("#Adventure.TextSpans", TinyMsg.parse("<gradient:yellow:green><b> Modalita Avventura </b></gradient>"));

        evt.addEventBinding(CustomUIEventBindingType.Activating, "#JoinAdventure");
    }

    @Override
    public void handleDataEvent(
        @Nonnull Ref<EntityStore> ref,
        @Nonnull Store<EntityStore> store,
        @Nonnull UIEventBind data
    ) {
        Player player = store.getComponent(ref, Player.getComponentType());

        if(player != null) {
            player.sendMessage(TinyMsg.parse("Playere Teletrasportato: " + player.getDisplayName()));
            player.getPageManager().setPage(ref, store, Page.None);
        }

    }

}
