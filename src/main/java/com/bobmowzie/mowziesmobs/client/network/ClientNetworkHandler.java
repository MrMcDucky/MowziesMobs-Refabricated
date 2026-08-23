package com.bobmowzie.mowziesmobs.client.network;

import com.bobmowzie.mowziesmobs.fabric.network.PayloadContext;
import com.bobmowzie.mowziesmobs.server.message.*;
import com.ilexiconn.llibrary.server.network.AnimationMessage;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.world.entity.player.Player;

/** Client-only receiver registration. Kept out of NetworkHandler so dedicated servers never link ClientPlayNetworking. */
public final class ClientNetworkHandler {
    private ClientNetworkHandler() {}

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(MessageUseAbility.TYPE, (payload, context) -> MessageUseAbility.handleClient(payload, context(context.player(), context.client()::execute)));
        ClientPlayNetworking.registerGlobalReceiver(MessageUpdateBossBar.TYPE, (payload, context) -> MessageUpdateBossBar.handleClient(payload, context(context.player(), context.client()::execute)));
        ClientPlayNetworking.registerGlobalReceiver(MessageSunblockEffect.TYPE, (payload, context) -> MessageSunblockEffect.handleClient(payload, context(context.player(), context.client()::execute)));
        ClientPlayNetworking.registerGlobalReceiver(MessageLinkEntities.TYPE, (payload, context) -> MessageLinkEntities.handleClient(payload, context(context.player(), context.client()::execute)));
        ClientPlayNetworking.registerGlobalReceiver(MessageInterruptAbility.TYPE, (payload, context) -> MessageInterruptAbility.handleClient(payload, context(context.player(), context.client()::execute)));
        ClientPlayNetworking.registerGlobalReceiver(MessageFreezeEffect.TYPE, (payload, context) -> MessageFreezeEffect.handleClient(payload, context(context.player(), context.client()::execute)));
        ClientPlayNetworking.registerGlobalReceiver(MessageBlackPinkInYourArea.TYPE, (payload, context) -> MessageBlackPinkInYourArea.handleClient(payload, context(context.player(), context.client()::execute)));
        ClientPlayNetworking.registerGlobalReceiver(MessageJumpToAbilitySectionServerToClient.TYPE, (payload, context) -> MessageJumpToAbilitySectionServerToClient.handleClient(payload, context(context.player(), context.client()::execute)));
        ClientPlayNetworking.registerGlobalReceiver(AnimationMessage.TYPE, (payload, context) -> AnimationMessage.handleClient(payload, context(context.player(), context.client()::execute)));
        ClientPlayNetworking.registerGlobalReceiver(MessageAddInProgressCooldown.TYPE, (payload, context) -> MessageAddInProgressCooldown.handleClient(payload, context(context.player(), context.client()::execute)));
    }

    private static PayloadContext context(Player player, java.util.function.Consumer<Runnable> executor) {
        return new PayloadContext() {
            @Override public void enqueueWork(Runnable work) { executor.accept(work); }
            @Override public Player player() { return player; }
        };
    }
}
