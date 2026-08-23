package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.bobmowzie.mowziesmobs.client.model.entity.ModelBipedAnimated;
import com.bobmowzie.mowziesmobs.client.render.entity.MowzieGeoArmorRenderer;
import com.bobmowzie.mowziesmobs.client.model.LayerHandler;
import com.bobmowzie.mowziesmobs.client.model.armor.WroughtHelmModel;
import com.bobmowzie.mowziesmobs.client.render.item.RenderGeomancerArmor;
import com.bobmowzie.mowziesmobs.client.render.item.RenderSolVisageArmor;
import com.bobmowzie.mowziesmobs.client.render.item.RenderUmvuthanaMaskArmor;
import com.bobmowzie.mowziesmobs.server.item.ItemGeomancerArmor;
import com.bobmowzie.mowziesmobs.server.item.ItemSolVisage;
import com.bobmowzie.mowziesmobs.server.item.ItemUmvuthanaMask;
import com.bobmowzie.mowziesmobs.server.item.ItemWroughtHelm;
import com.bobmowzie.mowziesmobs.mixin.client.HumanoidArmorLayerAccess;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import io.github.fabricators_of_create.porting_lib.item.client.ItemClientHooks;
import org.jetbrains.annotations.NotNull;

public class GeckoArmorLayer<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends HumanoidArmorLayer<T, M, A> {
    public GeckoArmorLayer(RenderLayerParent<T, M> layerParent, A innerModel, A outerModel, ModelManager modelManager) {
        super(layerParent, innerModel, outerModel, modelManager);
    }

    @Override // Used to skip GeckoLib Mixin hook 'InternalUtil#tryRenderGeoArmorPiece'
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, @NotNull T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        this.renderArmorPiece(poseStack, buffer, livingEntity, EquipmentSlot.CHEST, packedLight, this.getArmorModel(EquipmentSlot.CHEST), limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        this.renderArmorPiece(poseStack, buffer, livingEntity, EquipmentSlot.LEGS, packedLight, this.getArmorModel(EquipmentSlot.LEGS), limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        this.renderArmorPiece(poseStack, buffer, livingEntity, EquipmentSlot.FEET, packedLight, this.getArmorModel(EquipmentSlot.FEET), limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        this.renderArmorPiece(poseStack, buffer, livingEntity, EquipmentSlot.HEAD, packedLight, this.getArmorModel(EquipmentSlot.HEAD), limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
    }

    // Custom logic (has things switched around compared to vanilla)
    protected void renderArmorPiece(PoseStack poseStack, MultiBufferSource bufferSource, T livingEntity, EquipmentSlot slot, int packedLight, A baseModel, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack itemstack = livingEntity.getItemBySlot(slot);

        if (itemstack.getItem() instanceof ArmorItem armoritem) {
            if (livingEntity.getEquipmentSlotForItem(itemstack) == slot) {
                Model model = getArmorModelHook(livingEntity, itemstack, slot, baseModel, bufferSource, partialTick, limbSwing, limbSwingAmount, netHeadYaw, headPitch);

                if (!(model instanceof HumanoidModel<?>)) {
                    return;
                }

                getParentModel().copyPropertiesTo(baseModel);
                getParentModel().copyPropertiesTo((HumanoidModel<T>) model);

                setPartVisibility(baseModel, slot);
                setPartVisibility((A) model, slot);

                boolean usesInnerModel = usesInnerModel(slot);
                ArmorMaterial armormaterial = armoritem.getMaterial().value();

                ModelBipedAnimated.setUseMatrixMode(baseModel, true);

                for (int layerIdx = 0; layerIdx < armormaterial.layers().size(); layerIdx++) {
                    ArmorMaterial.Layer armormaterial$layer = armormaterial.layers().get(layerIdx);
                    ResourceLocation texture = ItemClientHooks.getArmorTexture(livingEntity, itemstack, armormaterial$layer, usesInnerModel, slot);
                    renderModel(poseStack, bufferSource, packedLight, model, 0xFFFFFFFF, texture);
                }

                ArmorTrim armortrim = itemstack.get(DataComponents.TRIM);

                if (armortrim != null) {
                    ModelBipedAnimated.setUseMatrixMode(baseModel, true);
                    ((HumanoidArmorLayerAccess) this).mowziesmobs$renderTrim(armoritem.getMaterial(), poseStack, bufferSource, packedLight, armortrim, (HumanoidModel<?>) model, usesInnerModel);
                }

                if (itemstack.hasFoil()) {
                    ModelBipedAnimated.setUseMatrixMode(baseModel, true);
                    ((HumanoidArmorLayerAccess) this).mowziesmobs$renderGlint(poseStack, bufferSource, packedLight, (HumanoidModel<?>) model);
                }
            }
        }
    }

    private MowzieGeoArmorRenderer<?> geomancerRenderer;
    private MowzieGeoArmorRenderer<?> solVisageRenderer;
    private MowzieGeoArmorRenderer<?> umvuthanaMaskRenderer;
    private HumanoidModel<?> wroughtHelmModel;

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Model getArmorModelHook(T entity, ItemStack stack, EquipmentSlot slot, A fallback, MultiBufferSource bufferSource, float partialTick, float limbSwing, float limbSwingAmount, float netHeadYaw, float headPitch) {
        if (stack.getItem() instanceof ItemGeomancerArmor) {
            if (geomancerRenderer == null) geomancerRenderer = new RenderGeomancerArmor();
            geomancerRenderer.prepForRender(entity, stack, slot, fallback, bufferSource, partialTick, limbSwing, limbSwingAmount, netHeadYaw, headPitch);
            return geomancerRenderer;
        }
        if (stack.getItem() instanceof ItemSolVisage) {
            if (solVisageRenderer == null) solVisageRenderer = new RenderSolVisageArmor();
            solVisageRenderer.prepForRender(entity, stack, slot, fallback, bufferSource, partialTick, limbSwing, limbSwingAmount, netHeadYaw, headPitch);
            return solVisageRenderer;
        }
        if (stack.getItem() instanceof ItemUmvuthanaMask) {
            if (umvuthanaMaskRenderer == null) umvuthanaMaskRenderer = new RenderUmvuthanaMaskArmor();
            umvuthanaMaskRenderer.prepForRender(entity, stack, slot, fallback, bufferSource, partialTick, limbSwing, limbSwingAmount, netHeadYaw, headPitch);
            return umvuthanaMaskRenderer;
        }
        if (stack.getItem() instanceof ItemWroughtHelm) {
            if (wroughtHelmModel == null) {
                wroughtHelmModel = new WroughtHelmModel<>(net.minecraft.client.Minecraft.getInstance().getEntityModels().bakeLayer(LayerHandler.WROUGHT_HELM_LAYER));
            }
            return wroughtHelmModel;
        }
        return fallback;
    }

    private void renderModel(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Model model, int color, ResourceLocation armorResource) {
        VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.armorCutoutNoCull(armorResource));

        // Custom logic
        if (model instanceof MowzieGeoArmorRenderer<?> mowzieRenderer) {
            mowzieRenderer.usingCustomPlayerAnimations = true;
        }

        model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, color);
    }
}
