package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.MaskType;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.fabricators_of_create.porting_lib.models.PortingLibModels;
import io.github.fabricators_of_create.porting_lib.models.TransformTypeDependentItemBakedModel;
import io.github.fabricators_of_create.porting_lib.models.events.client.ModelEvent;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class MMModels {
    public static final String[] HAND_MODEL_ITEMS = new String[] {"wrought_axe", "spear", "earthrend_gauntlet", "sculptor_staff"};
    public static final String PREFIX = "item/";

    public static void onModelBakeEvent(ModelEvent.ModifyBakingResult event) {
        Map<ModelResourceLocation, BakedModel> models = event.getModels();

        for (String item : HAND_MODEL_ITEMS) {
            ModelResourceLocation modelInventory = ModelResourceLocation.inventory(ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, item));
            ModelResourceLocation modelHand = prefixed(item + "_in_hand");
            BakedModel bakedModelDefault = models.get(modelInventory);
            BakedModel bakedModelHand = models.get(modelHand);
            if (bakedModelDefault != null && bakedModelHand != null) {
                models.put(modelInventory, new PerspectiveModel(
                        bakedModelDefault,
                        bakedModelHand,
                        context -> context == ItemDisplayContext.FIRST_PERSON_LEFT_HAND
                                || context == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND
                                || context == ItemDisplayContext.THIRD_PERSON_LEFT_HAND
                                || context == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND
                ));
            }
        }

        for (MaskType type : MaskType.values()) {
            ModelResourceLocation maskModelInventory = ModelResourceLocation.inventory(ResourceLocation.fromNamespaceAndPath(MMCommon.MODID,"umvuthana_mask_" + type.name));
            ModelResourceLocation maskModelFrame = prefixed("umvuthana_mask_" + type.name + "_frame");
            bakeMask(models, maskModelInventory, maskModelFrame);
        }
        ModelResourceLocation maskModelInventory = ModelResourceLocation.inventory(ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, "sol_visage"));
        ModelResourceLocation maskModelFrame = prefixed("sol_visage_frame");
        bakeMask(models, maskModelInventory, maskModelFrame);
    }

    private static void bakeMask(Map<ModelResourceLocation, BakedModel> map, ModelResourceLocation maskModelInventory, ModelResourceLocation maskModelFrame) {
        BakedModel maskBakedModelDefault = map.get(maskModelInventory);
        BakedModel maskBakedModelFrame = map.get(maskModelFrame);
        if (maskBakedModelDefault != null && maskBakedModelFrame != null) {
            map.put(maskModelInventory, new PerspectiveModel(maskBakedModelDefault, maskBakedModelFrame, context -> context == ItemDisplayContext.FIXED));
        }
    }

    private static final class PerspectiveModel implements BakedModel, TransformTypeDependentItemBakedModel {
        private final BakedModel base;
        private final BakedModel alternate;
        private final Predicate<ItemDisplayContext> useAlternate;

        private PerspectiveModel(BakedModel base, BakedModel alternate, Predicate<ItemDisplayContext> useAlternate) {
            this.base = base;
            this.alternate = alternate;
            this.useAlternate = useAlternate;
        }

        @Override
        public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand) {
            return base.getQuads(state, side, rand);
        }

        @Override
        public boolean useAmbientOcclusion() {
            return base.useAmbientOcclusion();
        }

        @Override
        public boolean isGui3d() {
            return base.isGui3d();
        }

        @Override
        public boolean usesBlockLight() {
            return base.usesBlockLight();
        }

        @Override
        public boolean isCustomRenderer() {
            return base.isCustomRenderer();
        }

        @Override
        public TextureAtlasSprite getParticleIcon() {
            return base.getParticleIcon();
        }

        @Override
        public ItemTransforms getTransforms() {
            return base.getTransforms();
        }

        @Override
        public ItemOverrides getOverrides() {
            return base.getOverrides();
        }

        @Override
        public BakedModel applyTransform(ItemDisplayContext context, PoseStack poseStack, boolean leftHand, DefaultTransform defaultTransform) {
            BakedModel selected = useAlternate.test(context) ? alternate : base;
            BakedModel transformed = TransformTypeDependentItemBakedModel.maybeApplyTransform(selected, context, poseStack, leftHand, defaultTransform);
            if (transformed != null) {
                return transformed;
            }
            defaultTransform.apply(selected);
            return selected;
        }
    }

    public static ModelResourceLocation prefixed(String path) {
        return PortingLibModels.standalone(ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, PREFIX + path));
    }
}
