package com.bobmowzie.mowziesmobs.client.gui;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.client.ClientProxy;
import com.bobmowzie.mowziesmobs.mixin.client.BossHealthOverlayAccess;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.BossEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CustomBossBar {
    private static final int VANILLA_VERTICAL_INCREMENT = 19;

    public static final Map<ResourceLocation, CustomBossBar> customBossBars = new HashMap<>();
    static {
        customBossBars.put(BuiltInRegistries.ENTITY_TYPE.getKey(EntityHandler.UMVUTHI.get()), new CustomBossBar(
                MMCommon.resource("textures/gui/boss_bar/umvuthi_bar_base.png"),
                MMCommon.resource("textures/gui/boss_bar/umvuthi_bar_overlay.png"),
                4, 8, 5, -5, -6, 256, 16, 25, ChatFormatting.GOLD));
        customBossBars.put(BuiltInRegistries.ENTITY_TYPE.getKey(EntityHandler.FROSTMAW.get()), new CustomBossBar(
                MMCommon.resource("textures/gui/boss_bar/frostmaw_bar_base.png"),
                MMCommon.resource("textures/gui/boss_bar/frostmaw_bar_overlay.png"),
                10, 32, 2, -4, -3, 256, 32, 25, ChatFormatting.WHITE));
        customBossBars.put(BuiltInRegistries.ENTITY_TYPE.getKey(EntityHandler.WROUGHTNAUT.get()), new CustomBossBar(
                MMCommon.resource("textures/gui/boss_bar/wroughtnaut_bar_base.png"),
                MMCommon.resource("textures/gui/boss_bar/wroughtnaut_bar_overlay.png"),
                4, 8, 5, -5, -6, 256, 16, 25, ChatFormatting.RED));
    }

    private final ResourceLocation baseTexture;
    private final ResourceLocation overlayTexture;
    private final boolean hasOverlay;
    private final int baseHeight;
    private final int baseTextureHeight;
    private final int baseOffsetY;
    private final int overlayOffsetX;
    private final int overlayOffsetY;
    private final int overlayWidth;
    private final int overlayHeight;
    private final int verticalIncrement;
    private final ChatFormatting textColor;

    public CustomBossBar(ResourceLocation baseTexture, ResourceLocation overlayTexture, int baseHeight,
                         int baseTextureHeight, int baseOffsetY, int overlayOffsetX, int overlayOffsetY,
                         int overlayWidth, int overlayHeight, int verticalIncrement, ChatFormatting textColor) {
        this.baseTexture = baseTexture;
        this.overlayTexture = overlayTexture;
        this.hasOverlay = overlayTexture != null;
        this.baseHeight = baseHeight;
        this.baseTextureHeight = baseTextureHeight;
        this.baseOffsetY = baseOffsetY;
        this.overlayOffsetX = overlayOffsetX;
        this.overlayOffsetY = overlayOffsetY;
        this.overlayWidth = overlayWidth;
        this.overlayHeight = overlayHeight;
        this.verticalIncrement = verticalIncrement;
        this.textColor = textColor;
    }

    /**
     * Re-renders the boss overlay only when at least one currently visible bar belongs to Mowzie's Mobs.
     * Vanilla/other-mod bars remain vanilla and can coexist with the custom bars.
     */
    public static boolean renderOverlay(GuiGraphics graphics) {
        Minecraft minecraft = Minecraft.getInstance();
        BossHealthOverlay overlay = minecraft.gui.getBossOverlay();
        BossHealthOverlayAccess access = (BossHealthOverlayAccess) overlay;
        Map<UUID, LerpingBossEvent> events = access.mowziesmobs$getEvents();

        boolean hasCustomBar = events.keySet().stream().anyMatch(id -> {
            ResourceLocation entityId = ClientProxy.bossBarRegistryNames.get(id);
            return entityId != null && customBossBars.containsKey(entityId);
        });
        if (!hasCustomBar) return false;

        int x = graphics.guiWidth() / 2 - 91;
        int y = 12;
        for (LerpingBossEvent bossEvent : events.values()) {
            ResourceLocation entityId = ClientProxy.bossBarRegistryNames.get(bossEvent.getId());
            CustomBossBar customBossBar = entityId == null ? null : customBossBars.get(entityId);

            if (customBossBar != null) {
                customBossBar.renderBossBar(graphics, x, y, bossEvent);
                y += customBossBar.verticalIncrement;
            } else {
                access.mowziesmobs$drawBar(graphics, x, y, bossEvent);
                drawCenteredName(graphics, bossEvent.getName(), y);
                y += VANILLA_VERTICAL_INCREMENT;
            }

            if (y >= graphics.guiHeight() / 3) break;
        }
        return true;
    }

    private void renderBossBar(GuiGraphics graphics, int x, int y, BossEvent event) {
        drawBar(graphics, x + 1, y + baseOffsetY, event);
        drawCenteredName(graphics, event.getName().copy().withStyle(textColor), y);

        if (hasOverlay) {
            graphics.blit(overlayTexture, x + 1 + overlayOffsetX, y + overlayOffsetY + baseOffsetY,
                    0, 0, overlayWidth, overlayHeight, overlayWidth, overlayHeight);
        }
    }

    private static void drawCenteredName(GuiGraphics graphics, Component component, int y) {
        Minecraft minecraft = Minecraft.getInstance();
        int width = minecraft.font.width(component);
        graphics.drawString(minecraft.font, component, graphics.guiWidth() / 2 - width / 2, y - 9, 0xFFFFFF);
    }

    private void drawBar(GuiGraphics graphics, int x, int y, BossEvent event) {
        graphics.blit(baseTexture, x, y, 0, 0, 182, baseHeight, 256, baseTextureHeight);
        int progressWidth = (int) (event.getProgress() * 183.0F);
        if (progressWidth > 0) {
            graphics.blit(baseTexture, x, y, 0, baseHeight, progressWidth, baseHeight, 256, baseTextureHeight);
        }
    }
}
