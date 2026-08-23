package com.bobmowzie.mowziesmobs.mixin;

import com.bobmowzie.mowziesmobs.server.world.feature.structure.StructureTypeHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpawnPlacements.class)
public abstract class SpawnPlacementsMixin {
    @Inject(method = "checkSpawnRules", at = @At("RETURN"), cancellable = true)
    private static <T extends Entity> void mowziesmobs$rejectMonasteryMonsterSpawns(
            EntityType<T> entityType, ServerLevelAccessor level, MobSpawnType spawnType,
            BlockPos pos, RandomSource random, CallbackInfoReturnable<Boolean> cir) {
        if (entityType.getCategory() != MobCategory.MONSTER || !cir.getReturnValue()) return;

        StructureManager structureManager = level.getLevel().structureManager();
        Structure monastery = structureManager.registryAccess()
                .registryOrThrow(Registries.STRUCTURE)
                .get(StructureTypeHandler.MONASTERY.getId());
        if (monastery == null || !structureManager.getStructureAt(pos, monastery).isValid()) return;

        BlockState ground = level.getBlockState(pos.below());
        if (level.canSeeSky(pos) && (ground.is(Blocks.DARK_OAK_PLANKS)
                || ground.is(Blocks.DARK_OAK_SLAB)
                || ground.is(Blocks.DARK_OAK_STAIRS))) {
            cir.setReturnValue(false);
        }
    }
}
