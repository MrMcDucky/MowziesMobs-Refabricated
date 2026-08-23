package com.bobmowzie.mowziesmobs.mixin.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.world.BossEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;
import java.util.UUID;

@Mixin(BossHealthOverlay.class)
public interface BossHealthOverlayAccess {
    @Accessor("events")
    Map<UUID, LerpingBossEvent> mowziesmobs$getEvents();

    @Invoker("drawBar")
    void mowziesmobs$drawBar(GuiGraphics graphics, int x, int y, BossEvent bossEvent);
}
