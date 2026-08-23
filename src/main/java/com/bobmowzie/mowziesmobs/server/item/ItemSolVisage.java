package com.bobmowzie.mowziesmobs.server.item;

import java.util.function.Consumer;

import software.bernie.geckolib.animatable.client.GeoRenderProvider;

import io.github.fabricators_of_create.porting_lib.item.extensions.PiglinsNeutralItem;

import io.github.fabricators_of_create.porting_lib.item.extensions.ArmorTextureItem;

import com.bobmowzie.mowziesmobs.client.render.entity.RenderUmvuthi;
import com.bobmowzie.mowziesmobs.client.render.item.RenderSolVisageArmor;
import com.bobmowzie.mowziesmobs.client.render.item.RenderSolVisageItem;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by BobMowzie on 8/15/2016.
 */
public class ItemSolVisage extends ArmorItem implements UmvuthanaMask, GeoItem, ArmorTextureItem, PiglinsNeutralItem {
    public String controllerName = "controller";
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public ItemSolVisage(Item.Properties properties) {
        super(MaterialHandler.SOL_VISAGE_MATERIAL, Type.HELMET, properties);
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        if (ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SOL_VISAGE.breakable.get()) return super.isValidRepairItem(toRepair, repair);
        return false;
    }

    @Override
    public boolean isEnchantable(ItemStack p_77616_1_) {
        return true;
    }

    @Override
    public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
        return true;
    }

    @Nullable
    @Override
    public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        return RenderUmvuthi.TEXTURE;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip, flagIn);
        tooltip.add(Component.translatable(getDescriptionId() + ".text.0").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(Component.translatable(getDescriptionId() + ".text.1").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(Component.translatable(getDescriptionId() + ".text.2").setStyle(ItemHandler.TOOLTIP_STYLE));
    }

    private PlayState predicate(AnimationState<ItemSolVisage> state) {
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, controllerName, 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private RenderSolVisageItem itemRenderer;
            private GeoArmorRenderer<?> armorRenderer;

            @Override
            public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (itemRenderer == null) itemRenderer = new RenderSolVisageItem();
                return itemRenderer;
            }

            @Override
            public <T extends LivingEntity> HumanoidModel<?> getGeoArmorRenderer(T livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<T> original) {
                if (armorRenderer == null) armorRenderer = new RenderSolVisageArmor();
                return armorRenderer;
            }
        });
    }
}

