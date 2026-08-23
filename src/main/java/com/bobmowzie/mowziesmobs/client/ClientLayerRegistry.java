package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.client.render.entity.FrozenRenderHandler;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.SunblockLayer;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoPlayer;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.mixin.client.LivingEntityRendererAccess;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import io.github.fabricators_of_create.porting_lib.client_events.event.client.EntityRenderersEvent;

import java.util.List;
import java.util.stream.Collectors;

// From https://github.com/Alex-the-666/AlexsMobs/blob/1.19/src/main/java/com/github/alexthe666/alexsmobs/client/ClientLayerRegistry.java
public class ClientLayerRegistry {
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        List<EntityType<? extends LivingEntity>> entityTypes = ImmutableList.copyOf(MMCommon.getLivingEntityTypes().collect(Collectors.toList()));
        entityTypes.forEach((entityType -> addLayerIfApplicable(entityType, event)));

        for (PlayerSkin.Model skin : event.getSkins()){
            if (event.getSkin(skin) instanceof LivingEntityRenderer<?, ?> renderer) {
                ((LivingEntityRendererAccess) renderer).mowziesmobs$addLayer(new FrozenRenderHandler.LayerFrozen(renderer));
                ((LivingEntityRendererAccess) renderer).mowziesmobs$addLayer(new SunblockLayer(renderer));
            }
        }

        GeckoPlayer.GeckoPlayerThirdPerson.initRenderer();
    }

    private static void addLayerIfApplicable(EntityType<? extends LivingEntity> entityType, EntityRenderersEvent.AddLayers event) {
        LivingEntityRenderer<?, ?> renderer = null;

        if (entityType != EntityType.ENDER_DRAGON) {
            try {
                if (event.getRenderer(entityType) instanceof LivingEntityRenderer<?,?> livingRenderer) {
                    renderer = livingRenderer;
                }
            } catch (Exception e) {
                if (!entityType.getBaseClass().isAssignableFrom(MowzieEntity.class)) {
                    MMCommon.LOGGER.warn("Could not apply layer to " + entityType.getDescriptionId() + ", has custom renderer that is not LivingEntityRenderer.");
                }
            }
            if (renderer != null) {
                ((LivingEntityRendererAccess) renderer).mowziesmobs$addLayer(new FrozenRenderHandler.LayerFrozen(renderer));
                ((LivingEntityRendererAccess) renderer).mowziesmobs$addLayer(new SunblockLayer(renderer));
            }
        }
    }
}
