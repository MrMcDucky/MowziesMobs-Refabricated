package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoModel;
import com.bobmowzie.mowziesmobs.client.render.item.RenderUmvuthanaMaskArmor;
import com.bobmowzie.mowziesmobs.server.item.ItemUmvuthanaMask;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthana;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import io.github.fabricators_of_create.porting_lib.item.client.ItemClientHooks;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class UmvuthanaArmorLayer extends GeoRenderLayer<EntityUmvuthana> {
    private final HumanoidModel defaultBipedModel;
    private final String boneName;

    public UmvuthanaArmorLayer(GeoRenderer<EntityUmvuthana> entityRendererIn, EntityRendererProvider.Context context, String boneName) {
        super(entityRendererIn);
        defaultBipedModel = new HumanoidModel(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR));
        this.boneName = boneName;
    }

    @Override
    public void render(PoseStack poseStack, EntityUmvuthana animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        super.render(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
        MowzieGeoModel<EntityUmvuthana> model = (MowzieGeoModel<EntityUmvuthana>) getGeoModel();
        MowzieGeoBone mask = model.getMowzieBone(boneName);
        if (!mask.isHidden()) {
            poseStack.pushPose();
            poseStack.setIdentity();
            poseStack.mulPose(mask.getPose());
            renderArmor(animatable, bufferSource, poseStack, packedLight, partialTick);
            bufferSource.getBuffer(renderType);
            poseStack.popPose();
        }
    }

    private void renderArmor(LivingEntity entityLivingBaseIn, MultiBufferSource bufferIn, PoseStack poseStack, int packedLightIn, float partialTick) {
        ItemStack itemStack = entityLivingBaseIn.getItemBySlot(EquipmentSlot.HEAD);
        if (itemStack.getItem() instanceof ArmorItem armoritem) {
            if (armoritem.getType() == ArmorItem.Type.HELMET) {
                boolean glintIn = itemStack.hasFoil();
                HumanoidModel<?> model = getArmorModelHook(entityLivingBaseIn, itemStack, EquipmentSlot.HEAD, defaultBipedModel, bufferIn, partialTick);
                armoritem.getMaterial().value().layers().forEach(layer -> {
                    ResourceLocation armorTexture = ItemClientHooks.getArmorTexture(entityLivingBaseIn, itemStack, layer, false, EquipmentSlot.HEAD);
                    if (armorTexture != null) {
                        VertexConsumer ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityCutoutNoCull(armorTexture), false, glintIn);
                        poseStack.pushPose();
                        poseStack.mulPose((new Quaternionf()).rotationXYZ(0.0F, 0.0F, (float) Math.PI));
                        poseStack.scale(1.511f, 1.511f, 1.511f);
                        poseStack.translate(0, -0.55, 0.15);
                        model.renderToBuffer(poseStack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, -1);
                        poseStack.popPose();
                    }
                });
            }
        }
    }

    private RenderUmvuthanaMaskArmor maskRenderer;

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected HumanoidModel<?> getArmorModelHook(LivingEntity entity, ItemStack itemStack, EquipmentSlot slot, HumanoidModel model, MultiBufferSource bufferSource, float partialTick) {
        if (itemStack.getItem() instanceof ItemUmvuthanaMask) {
            if (maskRenderer == null) maskRenderer = new RenderUmvuthanaMaskArmor();
            maskRenderer.prepForRender(entity, itemStack, slot, model, bufferSource, partialTick, 0, 0, 0, 0);
            return maskRenderer;
        }
        return model;
    }
}

