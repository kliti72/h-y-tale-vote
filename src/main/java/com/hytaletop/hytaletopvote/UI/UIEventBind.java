package com.hytaletop.hytaletopvote.UI;

import javax.annotation.Nonnull;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class UIEventBind {
    public String playerName;

    @Nonnull
    public static final BuilderCodec<UIEventBind> CODEC = 
    BuilderCodec.builder(UIEventBind.class, UIEventBind::new)
    .append(
        new KeyedCodec<>("@PlayerName", Codec.STRING),
        (obj, val) -> obj.playerName = val,
        obj -> obj.playerName
    ).add().build();


}
