package com.bobmowzie.mowziesmobs.mixin.client;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntityRenderer.class)
public interface LivingEntityRendererAccess {
    @Invoker("addLayer")
    boolean mowziesmobs$addLayer(RenderLayer<?, ?> layer);
}
