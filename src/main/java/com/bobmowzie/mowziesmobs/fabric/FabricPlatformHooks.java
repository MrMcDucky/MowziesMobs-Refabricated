package com.bobmowzie.mowziesmobs.fabric;

import io.github.fabricators_of_create.porting_lib.blocks.extensions.CustomFrictionBlock;
import io.github.fabricators_of_create.porting_lib.blocks.extensions.CustomLadderBlock;
import io.github.fabricators_of_create.porting_lib.blocks.extensions.CustomSoundTypeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Small loader bridge for NeoForge-patched convenience methods that do not
 * physically exist on Fabric's vanilla classes. Keeping these calls here avoids
 * spreading loader conditionals through the gameplay code.
 */
public final class FabricPlatformHooks {
    private FabricPlatformHooks() {}

    public static float getFriction(BlockState state, LevelReader level, BlockPos pos, Entity entity) {
        if (state.getBlock() instanceof CustomFrictionBlock custom) {
            return custom.getFriction(state, level, pos, entity);
        }
        return state.getBlock().getFriction();
    }

    public static SoundType getSoundType(BlockState state, LevelReader level, BlockPos pos, Entity entity) {
        if (state.getBlock() instanceof CustomSoundTypeBlock custom) {
            return custom.getSoundType(state, level, pos, entity);
        }
        return state.getSoundType();
    }

    public static boolean isLadder(BlockState state, LevelReader level, BlockPos pos, LivingEntity entity) {
        if (state.getBlock() instanceof CustomLadderBlock custom) {
            return custom.isLadder(state, level, pos, entity);
        }
        return state.is(BlockTags.CLIMBABLE);
    }

    /** NeoForge allows vehicles to opt out of the normal sitting pose. */
    public static boolean shouldRiderSit(Entity vehicle) {
        return !(vehicle instanceof com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrozenController);
    }
}
