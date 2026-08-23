package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.client.gui.GuiSculptorTrade;
import com.bobmowzie.mowziesmobs.client.gui.GuiUmvuthanaTrade;
import com.bobmowzie.mowziesmobs.client.gui.GuiUmvuthiTrade;
import com.bobmowzie.mowziesmobs.client.render.block.GongRenderer;
import com.bobmowzie.mowziesmobs.client.render.entity.*;
import com.bobmowzie.mowziesmobs.server.block.entity.BlockEntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.MaskType;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerHandler;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import io.github.fabricators_of_create.porting_lib.models.events.client.ModelEvent;

public class ClientEventBusSubscriber {

    public static void registerRenderers() {
            EntityRendererRegistry.register(EntityHandler.BABY_FOLIAATH.get(), RenderFoliaathBaby::new);
            EntityRendererRegistry.register(EntityHandler.FOLIAATH.get(), RenderFoliaath::new);
            EntityRendererRegistry.register(EntityHandler.WROUGHTNAUT.get(), RenderWroughtnaut::new);
            EntityRendererRegistry.register(EntityHandler.UMVUTHI.get(), RenderUmvuthi::new);
            EntityRendererRegistry.register(EntityHandler.UMVUTHANA_RAPTOR.get(), RenderUmvuthana::new);
            EntityRendererRegistry.register(EntityHandler.UMVUTHANA_FOLLOWER_TO_RAPTOR.get(), RenderUmvuthana::new);
            EntityRendererRegistry.register(EntityHandler.UMVUTHANA_MINION.get(), RenderUmvuthana::new);
            EntityRendererRegistry.register(EntityHandler.UMVUTHANA_FOLLOWER_TO_PLAYER.get(), RenderUmvuthana::new);
            EntityRendererRegistry.register(EntityHandler.UMVUTHANA_CRANE_TO_PLAYER.get(), RenderUmvuthana::new);
            EntityRendererRegistry.register(EntityHandler.UMVUTHANA_CRANE.get(), RenderUmvuthana::new);
            EntityRendererRegistry.register(EntityHandler.FROSTMAW.get(), RenderFrostmaw::new);
            EntityRendererRegistry.register(EntityHandler.GROTTOL.get(), RenderGrottol::new);
            EntityRendererRegistry.register(EntityHandler.LANTERN.get(), RenderLantern::new);
            EntityRendererRegistry.register(EntityHandler.NAGA.get(), RenderNaga::new);
            EntityRendererRegistry.register(EntityHandler.SCULPTOR.get(), RenderSculptor::new);
            EntityRendererRegistry.register(EntityHandler.BLUFF.get(), RenderBluff::new);
            EntityRendererRegistry.register(EntityHandler.ELOKOSA_FOLLOWER_TO_HOWLER.get(), RenderElokosa::new);
            EntityRendererRegistry.register(EntityHandler.ELOKOSA_HOWLER.get(), RenderElokosa::new);

            EntityRendererRegistry.register(EntityHandler.DART.get(), RenderDart::new);
            EntityRendererRegistry.register(EntityHandler.SUNSTRIKE.get(), RenderSunstrike::new);
            EntityRendererRegistry.register(EntityHandler.SOLAR_BEAM.get(), RenderSolarBeam::new);
            EntityRendererRegistry.register(EntityHandler.BOULDER_PROJECTILE.get(), RenderBoulder::new);
            EntityRendererRegistry.register(EntityHandler.BOULDER_SCULPTOR.get(), RenderBoulder::new);
            EntityRendererRegistry.register(EntityHandler.BOULDER_SCULPTOR_CRUMBLING.get(), RenderBoulder::new);
            EntityRendererRegistry.register(EntityHandler.PILLAR.get(), RenderPillar::new);
            EntityRendererRegistry.register(EntityHandler.PILLAR_SCULPTOR.get(), RenderPillar::new);
            EntityRendererRegistry.register(EntityHandler.PILLAR_PIECE.get(), RenderNothing::new);
            EntityRendererRegistry.register(EntityHandler.AXE_ATTACK.get(), RenderAxeAttack::new);
            EntityRendererRegistry.register(EntityHandler.POISON_BALL.get(), RenderPoisonBall::new);
            EntityRendererRegistry.register(EntityHandler.ICE_BALL.get(), RenderIceBall::new);
            EntityRendererRegistry.register(EntityHandler.ICE_BREATH.get(), RenderNothing::new);
            EntityRendererRegistry.register(EntityHandler.FROZEN_CONTROLLER.get(), RenderNothing::new);
            EntityRendererRegistry.register(EntityHandler.SUPER_NOVA.get(), RenderSuperNova::new);
            EntityRendererRegistry.register(EntityHandler.FALLING_BLOCK.get(), RenderFallingBlock::new);
            EntityRendererRegistry.register(EntityHandler.BLOCK_SWAPPER.get(), RenderNothing::new);
            EntityRendererRegistry.register(EntityHandler.BLOCK_SWAPPER_TUNNELING.get(), RenderNothing::new);
            EntityRendererRegistry.register(EntityHandler.CAMERA_SHAKE.get(), RenderNothing::new);
            EntityRendererRegistry.register(EntityHandler.ROCK_SLING.get(), RenderRockSling::new);
            EntityRendererRegistry.register(EntityHandler.FISSURE.get(), RenderNothing::new);
            EntityRendererRegistry.register(EntityHandler.FISSURE_PIECE.get(), RenderFissurePiece::new);
            EntityRendererRegistry.register(EntityHandler.EARTH_SPIKE.get(), RenderEarthSpike::new);

            BlockEntityRendererRegistry.register(BlockEntityHandler.GONG_BLOCK_ENTITY.get(), GongRenderer::new);
    }

    public static void registerMenuScreens() {
        net.minecraft.client.gui.screens.MenuScreens.register(ContainerHandler.CONTAINER_UMVUTHANA_TRADE.get(), GuiUmvuthanaTrade::new);
        net.minecraft.client.gui.screens.MenuScreens.register(ContainerHandler.CONTAINER_UMVUTHI_TRADE.get(), GuiUmvuthiTrade::new);
        net.minecraft.client.gui.screens.MenuScreens.register(ContainerHandler.CONTAINER_SCULPTOR_TRADE.get(), GuiSculptorTrade::new);
    }

    public static void onRegisterModels(ModelEvent.RegisterAdditional modelRegistryEvent) {
        for (String item : MMModels.HAND_MODEL_ITEMS) {
        	modelRegistryEvent.register(MMModels.prefixed(item + "_in_hand"));
        }

        for (MaskType type : MaskType.values()) {
        	modelRegistryEvent.register(MMModels.prefixed("umvuthana_mask_" + type.name + "_frame"));
        }

        modelRegistryEvent.register(MMModels.prefixed("sol_visage_frame"));
    }
}