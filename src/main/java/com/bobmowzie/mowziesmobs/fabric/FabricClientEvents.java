package com.bobmowzie.mowziesmobs.fabric;

import com.bobmowzie.mowziesmobs.client.ClientEventHandler;
import io.github.fabricators_of_create.porting_lib.client_events.event.client.ComputeFovModifierEvent;
import io.github.fabricators_of_create.porting_lib.client_events.event.client.InputEvent;
import io.github.fabricators_of_create.porting_lib.client_events.event.client.RenderHandEvent;
import io.github.fabricators_of_create.porting_lib.client_events.event.client.ViewportEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.tick.PlayerTickEvent;
import io.github.fabricators_of_create.porting_lib.event.client.RenderFrameEvent;
import io.github.fabricators_of_create.porting_lib.event.client.InteractEvents;
import io.github.fabricators_of_create.porting_lib.event.client.RenderPlayerEvents;
import io.github.fabricators_of_create.porting_lib.gui.events.RenderGuiLayerCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

/** Client event wiring replacing NeoForge's annotation-driven client event bus. */
public final class FabricClientEvents {
    private static boolean registered;

    private FabricClientEvents() {}

    public static void register() {
        if (registered) return;
        registered = true;

        RenderHandEvent.EVENT.register(ClientEventHandler::onHandRender);
        RenderPlayerEvents.PRE.register(ClientEventHandler::onRenderPlayerPre);
        PlayerTickEvent.Post.EVENT.register(ClientEventHandler::onPlayerTick);
        RenderFrameEvent.PRE.register(ClientEventHandler::onRenderTick);
        ComputeFovModifierEvent.EVENT.register(ClientEventHandler::updateFOV);
        ViewportEvent.ComputeCameraAngles.EVENT.register(ClientEventHandler::onSetupCamera);
        InputEvent.InteractionKeyMappingTriggered.EVENT.register(ClientEventHandler::onInteractionKeyMappingTriggered);
        ClientTickEvents.END_WORLD_TICK.register(ClientEventHandler::onLevelTick);
        InteractEvents.PICK.register(ClientEventHandler::onPickBlock);
        WorldRenderEvents.AFTER_ENTITIES.register(ClientEventHandler::onRenderWorldAfterEntities);

        RenderGuiLayerCallback.POST.register((graphics, partialTick, name, layer) ->
                ClientEventHandler.onRenderOverlay(graphics, partialTick, name));
        RenderGuiLayerCallback.PRE.register((graphics, partialTick, name, layer) ->
                ClientEventHandler.onRenderHUD(graphics, partialTick, name));
    }
}
