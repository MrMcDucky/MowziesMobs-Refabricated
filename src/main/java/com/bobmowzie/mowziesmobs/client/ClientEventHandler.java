package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.client.gui.CustomBossBar;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelGeckoPlayerFirstPerson;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelGeckoPlayerThirdPerson;
import com.bobmowzie.mowziesmobs.client.render.MMRenderType;
import com.bobmowzie.mowziesmobs.client.render.block.SculptorBlockMarking;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoFirstPersonRenderer;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoRenderPlayer;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoPlayer;
import com.bobmowzie.mowziesmobs.server.capability.AbilityData;
import com.bobmowzie.mowziesmobs.server.capability.DataHandler;
import com.bobmowzie.mowziesmobs.server.capability.FrozenData;
import com.bobmowzie.mowziesmobs.server.capability.PlayerData;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityCameraShake;
import com.bobmowzie.mowziesmobs.server.entity.elokosa.EntityElokosaFollower;
import com.bobmowzie.mowziesmobs.server.entity.elokosa.EntityElokosaHowler;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrozenController;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthanaFollower;
import com.bobmowzie.mowziesmobs.server.item.ItemBlowgun;
import com.bobmowzie.mowziesmobs.client.sound.BossMusicPlayer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.fabricators_of_create.porting_lib.client_events.event.client.ComputeFovModifierEvent;
import io.github.fabricators_of_create.porting_lib.client_events.event.client.InputEvent;
import io.github.fabricators_of_create.porting_lib.client_events.event.client.RenderHandEvent;
import io.github.fabricators_of_create.porting_lib.client_events.event.client.ViewportEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.tick.PlayerTickEvent;
import io.github.fabricators_of_create.porting_lib.gui.layered.VanillaGuiLayers;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.CameraType;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public final class ClientEventHandler {
    private static final ResourceLocation FROZEN_BLUR = ResourceLocation.withDefaultNamespace("textures/misc/powder_snow_outline.png");
    private static final ResourceLocation SCULPTOR_BLOCK_GLOW = MMCommon.resource("textures/entity/sculptor_highlight.png");

    private ClientEventHandler() {}

    public static void onHandRender(RenderHandEvent event) {
        if (!ConfigHandler.CLIENT.customPlayerAnims.get()) return;
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        AbilityData data = DataHandler.getData(player, DataHandler.ABILITY_DATA);
        if (data.getActiveAbility() == null) return;

        GeckoPlayer.GeckoPlayerFirstPerson geckoPlayer = GeckoFirstPersonRenderer.GECKO_PLAYER_FIRST_PERSON;
        if (geckoPlayer == null) return;

        ModelGeckoPlayerFirstPerson model = (ModelGeckoPlayerFirstPerson) geckoPlayer.getModel();
        GeckoFirstPersonRenderer renderer = (GeckoFirstPersonRenderer) geckoPlayer.getPlayerRenderer();
        if (model == null || renderer == null) return;

        if (!model.isUsingSmallArms() && player.getSkin().model().name().equals("slim")) {
            renderer.setSmallArms();
        }
        event.setCanceled(true);
        float delta = event.getPartialTick();
        float pitch = Mth.lerp(delta, player.xRotO, player.getXRot());
        renderer.renderItemInFirstPerson(player, pitch, delta, event.getHand(), event.getSwingProgress(),
                event.getItemStack(), event.getEquipProgress(), event.getPoseStack(), event.getMultiBufferSource(),
                event.getPackedLight(), geckoPlayer);
    }

    public static boolean onRenderPlayerPre(Player player, PlayerRenderer vanillaRenderer, float partialTick,
                                            PoseStack poseStack, MultiBufferSource buffers, int packedLight) {
        if (!(player instanceof AbstractClientPlayer clientPlayer)) return false;
        if (!ConfigHandler.CLIENT.customPlayerAnims.get()) return false;
        if (ConfigHandler.CLIENT.hidePlayerAnimsInFirstPerson.get()
                && player == Minecraft.getInstance().player
                && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON) {
            return false;
        }

        AbilityData abilityData = DataHandler.getData(player, DataHandler.ABILITY_DATA);
        if (abilityData.getActiveAbility() == null) return false;

        GeckoPlayer.GeckoPlayerThirdPerson geckoPlayer = DataHandler.getData(player, DataHandler.PLAYER_DATA).getGeckoPlayer();
        if (geckoPlayer == null) return false;

        ModelGeckoPlayerThirdPerson model = (ModelGeckoPlayerThirdPerson) geckoPlayer.getModel();
        GeckoRenderPlayer renderer = (GeckoRenderPlayer) geckoPlayer.getPlayerRenderer();
        if (model == null || renderer == null) return false;

        renderer.render(clientPlayer, player.getYRot(), partialTick, poseStack, buffers, packedLight, geckoPlayer);
        return true;
    }

    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide()) return;

        GeckoPlayer geckoPlayer = DataHandler.getData(player, DataHandler.PLAYER_DATA).getGeckoPlayer();
        if (geckoPlayer != null) geckoPlayer.tick();
        if (player == Minecraft.getInstance().player && GeckoFirstPersonRenderer.GECKO_PLAYER_FIRST_PERSON != null) {
            GeckoFirstPersonRenderer.GECKO_PLAYER_FIRST_PERSON.tick();
        }
    }

    public static void onRenderTick(DeltaTracker deltaTracker) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        FrozenData data = DataHandler.getData(player, DataHandler.FROZEN_DATA);
        if (data.getFrozen() && data.getPrevFrozen()) {
            player.setYRot(data.getFrozenYaw());
            player.setXRot(data.getFrozenPitch());
            player.yHeadRot = data.getFrozenYawHead();
            player.yRotO = player.getYRot();
            player.xRotO = player.getXRot();
            player.yHeadRotO = player.yHeadRot;
        }
    }

    public static void onRenderOverlay(GuiGraphics graphics, DeltaTracker partialTick, ResourceLocation name) {
        if (!name.equals(VanillaGuiLayers.CAMERA_OVERLAYS)) return;
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        FrozenData data = DataHandler.getData(player, DataHandler.FROZEN_DATA);
        if (data.getFrozen() && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON) {
            graphics.blit(FROZEN_BLUR, 0, 0, 0, 0, graphics.guiWidth(), graphics.guiHeight(), graphics.guiWidth(), graphics.guiHeight());
        }
    }

    /** @return true when the underlying GUI layer should be cancelled. */
    public static boolean onRenderHUD(GuiGraphics graphics, DeltaTracker partialTick, ResourceLocation name) {
        if (name.equals(VanillaGuiLayers.BOSS_OVERLAY)
                && ConfigHandler.CLIENT.customBossBars.get()
                && CustomBossBar.renderOverlay(graphics)) {
            return true;
        }

        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && player.isPassenger() && player.getVehicle() instanceof EntityFrozenController) {
            if (name.equals(VanillaGuiLayers.VEHICLE_HEALTH)) return true;
            Minecraft.getInstance().gui.setOverlayMessage(Component.empty(), false);
        }
        return false;
    }

    public static void updateFOV(ComputeFovModifierEvent event) {
        Player player = event.getPlayer();
        if (player.isUsingItem() && player.getUseItem().getItem() instanceof ItemBlowgun) {
            int i = player.getTicksUsingItem();
            float pull = (float) i / 5.0F;
            if (pull > 1.0F) pull = 1.0F;
            else pull *= pull;
            event.setNewFovModifier(1.0F - pull * 0.15F);
        }
    }

    public static void onSetupCamera(ViewportEvent.ComputeCameraAngles event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        float delta = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);
        float ticksExistedDelta = player.tickCount + delta;

        if (ConfigHandler.CLIENT.doCameraShakes.get() && !Minecraft.getInstance().isPaused()) {
            float shakeAmplitude = 0;
            for (EntityCameraShake cameraShake : player.level().getEntitiesOfClass(EntityCameraShake.class, player.getBoundingBox().inflate(20, 20, 20))) {
                if (cameraShake.distanceTo(player) < cameraShake.getRadius()) {
                    shakeAmplitude += cameraShake.getShakeAmount(player, delta);
                }
            }
            shakeAmplitude = Math.min(shakeAmplitude, 1.0f);
            event.setPitch((float) (event.getPitch() + shakeAmplitude * Math.cos(ticksExistedDelta * 3 + 2) * 25));
            event.setYaw((float) (event.getYaw() + shakeAmplitude * Math.cos(ticksExistedDelta * 5 + 1) * 25));
            event.setRoll((float) (event.getRoll() + shakeAmplitude * Math.cos(ticksExistedDelta * 4) * 25));
        }
    }

    public static void onRenderWorldAfterEntities(WorldRenderContext context) {
        ClientLevel level = Minecraft.getInstance().level;
        PoseStack poseStack = context.matrixStack();
        MultiBufferSource consumers = context.consumers();
        if (level == null || Minecraft.getInstance().player == null || poseStack == null || consumers == null) return;

        Vec3 cameraPos = context.camera().getPosition();
        double cameraX = cameraPos.x();
        double cameraY = cameraPos.y();
        double cameraZ = cameraPos.z();
        float tick = level.getGameTime() + Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);

        for (Long2ObjectMap.Entry<SculptorBlockMarking> entry : ClientProxy.sculptorMarkedBlocks.long2ObjectEntrySet()) {
            BlockPos blockPos = BlockPos.of(entry.getLongKey());
            poseStack.pushPose();
            poseStack.translate(blockPos.getX() - cameraX, blockPos.getY() - cameraY, blockPos.getZ() - cameraZ);

            float blockOffset = (blockPos.getX() + blockPos.getY() + blockPos.getZ()) * 0.25F;
            VertexConsumer vertexConsumer = new SheetedDecalTextureGenerator(
                    consumers.getBuffer(MMRenderType.highlight(
                            SCULPTOR_BLOCK_GLOW,
                            tick * 0.02F + blockOffset,
                            tick * 0.01F + blockOffset)),
                    poseStack.last(), 0.25F);
            renderSculptorHighlight(level.getBlockState(blockPos), blockPos, level, poseStack, level.random, vertexConsumer);

            poseStack.popPose();
        }
    }

    private static void renderSculptorHighlight(BlockState state, BlockPos pos, BlockAndTintGetter level,
                                                 PoseStack poseStack, RandomSource random, VertexConsumer vertexConsumer) {
        if (state.getRenderShape() != RenderShape.MODEL) return;

        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
        BakedModel model = dispatcher.getBlockModel(state);
        dispatcher.getModelRenderer().tesselateBlock(
                level, model, state, pos, poseStack, vertexConsumer, true, random, state.getSeed(pos), OverlayTexture.NO_OVERLAY);
    }

    public static boolean onPickBlock(Minecraft minecraft, HitResult hit) {
        if (!(hit instanceof EntityHitResult entityHit) || minecraft.player == null || minecraft.gameMode == null) return false;
        if (!minecraft.player.getAbilities().instabuild) return false;

        ItemStack stack;
        if (entityHit.getEntity() instanceof EntityElokosaFollower follower) {
            stack = follower.getPickedResult(hit);
        } else if (entityHit.getEntity() instanceof EntityElokosaHowler howler) {
            stack = howler.getPickedResult(hit);
        } else if (entityHit.getEntity() instanceof EntityUmvuthanaFollower follower) {
            stack = follower.getPickedResult(hit);
        } else {
            return false;
        }

        if (stack.isEmpty()) return false;
        minecraft.player.getInventory().setPickedItem(stack);
        minecraft.gameMode.handleCreativeModeItemAdd(
                minecraft.player.getInventory().getSelected(),
                36 + minecraft.player.getInventory().selected);
        return true;
    }

    public static void onLevelTick(ClientLevel level) {
        MMCommon.PROXY.updateMarkedBlocks();
        BossMusicPlayer.tick();
    }

    public static void onInteractionKeyMappingTriggered(InputEvent.InteractionKeyMappingTriggered event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        PlayerData data = DataHandler.getData(player, DataHandler.PLAYER_DATA);
        if (event.getKeyMapping() == Minecraft.getInstance().options.keyAttack) {
            data.pressedAttackKey(player);
        } else if (event.getKeyMapping() == Minecraft.getInstance().options.keyUse) {
            data.pressedUseKey(player);
        }
    }
}
