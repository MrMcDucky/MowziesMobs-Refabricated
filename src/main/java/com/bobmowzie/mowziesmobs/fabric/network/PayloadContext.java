package com.bobmowzie.mowziesmobs.fabric.network;

import net.minecraft.world.entity.player.Player;

/**
 * Tiny loader-neutral facade used by the original packet handlers. NeoForge's
 * IPayloadContext only supplied two operations to Mowzie's Mobs: scheduling
 * work and obtaining the player. Keeping those semantics here lets the packet
 * payload records stay unchanged while Fabric owns the actual transport.
 */
public interface PayloadContext {
    void enqueueWork(Runnable work);
    Player player();
}
