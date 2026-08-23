package com.bobmowzie.mowziesmobs.client.render.item;

import com.bobmowzie.mowziesmobs.client.model.LayerHandler;
import com.bobmowzie.mowziesmobs.client.model.armor.WroughtHelmModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.fabricators_of_create.porting_lib.client.armor.ArmorRenderer;
import io.github.fabricators_of_create.porting_lib.item.client.ItemClientHooks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;

public final class RenderWroughtHelmArmor implements ArmorRenderer {
    private WroughtHelmModel<LivingEntity> model;

    private WroughtHelmModel<LivingEntity> getModel() {
        if (model == null) {
            model = new WroughtHelmModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(LayerHandler.WROUGHT_HELM_LAYER));
        }
        return model;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, ItemStack stack, LivingEntity entity,
                       EquipmentSlot slot, int packedLight, HumanoidModel<LivingEntity> contextModel,
                       HumanoidModel<LivingEntity> armorModel) {
        if (slot != EquipmentSlot.HEAD || !(stack.getItem() instanceof ArmorItem armorItem)) return;

        WroughtHelmModel<LivingEntity> wroughtModel = getModel();
        contextModel.copyPropertiesTo(wroughtModel);
        wroughtModel.setAllVisible(false);
        wroughtModel.head.visible = true;

        ArmorMaterial material = armorItem.getMaterial().value();
        for (ArmorMaterial.Layer layer : material.layers()) {
            ResourceLocation texture = ItemClientHooks.getArmorTexture(entity, stack, layer, false, slot);
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.armorCutoutNoCull(texture));
            wroughtModel.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        }

        ArmorTrim trim = stack.get(DataComponents.TRIM);
        if (trim != null) {
            TextureAtlasSprite sprite = Minecraft.getInstance().getModelManager()
                    .getAtlas(Sheets.ARMOR_TRIMS_SHEET)
                    .getSprite(trim.outerTexture(armorItem.getMaterial()));
            VertexConsumer trimConsumer = sprite.wrap(bufferSource.getBuffer(Sheets.armorTrimsSheet(trim.pattern().value().decal())));
            wroughtModel.renderToBuffer(poseStack, trimConsumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        }

        if (stack.hasFoil()) {
            wroughtModel.renderToBuffer(poseStack, bufferSource.getBuffer(RenderType.armorEntityGlint()),
                    packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        }
    }
}
