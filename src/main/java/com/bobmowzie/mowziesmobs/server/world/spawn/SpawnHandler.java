package com.bobmowzie.mowziesmobs.server.world.spawn;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.world.BiomeChecker;
import com.bobmowzie.mowziesmobs.server.world.feature.structure.StructureTypeHandler;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/** Fabric natural-spawn bridge for the original config-driven Mowzie spawn system. */
public final class SpawnHandler {
    public static final Map<EntityType<?>, ConfigHandler.SpawnConfig> SPAWN_CONFIGS = new HashMap<>();

    public static BiomeChecker FOLIAATH_BIOME_CHECKER;
    public static BiomeChecker UMVUTHANA_RAPTOR_BIOME_CHECKER;
    public static BiomeChecker GROTTOL_BIOME_CHECKER;
    public static BiomeChecker LANTERN_BIOME_CHECKER;
    public static BiomeChecker NAGA_BIOME_CHECKER;
    public static BiomeChecker BLUFF_BIOME_CHECKER;
    public static BiomeChecker ELOKOSA_HOWLER_BIOME_CHECKER;

    private static boolean biomeSpawnsRegistered = false;
    private static boolean structureBiomeRulesRegistered = false;

    private static final SpawnPlacementType MM_SPAWN = (level, position, type) -> {
        BlockState below = level.getBlockState(position.below());
        if (type == null || below.is(Blocks.BEDROCK) || below.is(Blocks.BARRIER) || !below.blocksMotion()) return false;
        BlockState state = level.getBlockState(position);
        if (!NaturalSpawner.isValidEmptySpawnBlock(level, position, state, state.getFluidState(), type)) return false;
        BlockState up = level.getBlockState(position.above());
        return NaturalSpawner.isValidEmptySpawnBlock(level, position.above(), up, up.getFluidState(), type);
    };

    private SpawnHandler() {}

    private static void populateSpawnConfigs() {
        if (!SPAWN_CONFIGS.isEmpty()) return;
        SPAWN_CONFIGS.put(EntityHandler.FOLIAATH.get(), ConfigHandler.COMMON.MOBS.FOLIAATH.spawnConfig);
        SPAWN_CONFIGS.put(EntityHandler.UMVUTHANA_RAPTOR.get(), ConfigHandler.COMMON.MOBS.UMVUTHANA.spawnConfig);
        SPAWN_CONFIGS.put(EntityHandler.LANTERN.get(), ConfigHandler.COMMON.MOBS.LANTERN.spawnConfig);
        SPAWN_CONFIGS.put(EntityHandler.NAGA.get(), ConfigHandler.COMMON.MOBS.NAGA.spawnConfig);
        SPAWN_CONFIGS.put(EntityHandler.GROTTOL.get(), ConfigHandler.COMMON.MOBS.GROTTOL.spawnConfig);
        SPAWN_CONFIGS.put(EntityHandler.BLUFF.get(), ConfigHandler.COMMON.MOBS.BLUFF.spawnConfig);
        SPAWN_CONFIGS.put(EntityHandler.ELOKOSA_HOWLER.get(), ConfigHandler.COMMON.MOBS.ELOKOSA.spawnConfig);
    }

    public static void registerSpawnPlacementTypes() {
        populateSpawnConfigs();
        SpawnPlacements.register(EntityHandler.FOLIAATH.get(), MM_SPAWN, Heightmap.Types.MOTION_BLOCKING, MowzieEntity::spawnPredicate);
        SpawnPlacements.register(EntityHandler.LANTERN.get(), MM_SPAWN, Heightmap.Types.MOTION_BLOCKING, MowzieEntity::spawnPredicate);
        SpawnPlacements.register(EntityHandler.UMVUTHANA_RAPTOR.get(), MM_SPAWN, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MowzieEntity::spawnPredicate);
        SpawnPlacements.register(EntityHandler.NAGA.get(), MM_SPAWN, Heightmap.Types.MOTION_BLOCKING, MowzieEntity::spawnPredicate);
        SpawnPlacements.register(EntityHandler.GROTTOL.get(), MM_SPAWN, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MowzieEntity::spawnPredicate);
        SpawnPlacements.register(EntityHandler.UMVUTHANA_CRANE.get(), MM_SPAWN, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MowzieEntity::spawnPredicate);
        SpawnPlacements.register(EntityHandler.BLUFF.get(), MM_SPAWN, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MowzieEntity::spawnPredicate);
        SpawnPlacements.register(EntityHandler.ELOKOSA_HOWLER.get(), MM_SPAWN, Heightmap.Types.MOTION_BLOCKING, MowzieEntity::spawnPredicate);
    }

    public static synchronized void registerBiomeSpawns() {
        if (biomeSpawnsRegistered) return;

        registerStructureBiomeRules();

        FOLIAATH_BIOME_CHECKER = registerBiomeSpawn(EntityHandler.FOLIAATH.get(), ConfigHandler.COMMON.MOBS.FOLIAATH.spawnConfig, MobCategory.MONSTER);
        UMVUTHANA_RAPTOR_BIOME_CHECKER = registerBiomeSpawn(EntityHandler.UMVUTHANA_RAPTOR.get(), ConfigHandler.COMMON.MOBS.UMVUTHANA.spawnConfig, MobCategory.MONSTER);
        GROTTOL_BIOME_CHECKER = registerBiomeSpawn(EntityHandler.GROTTOL.get(), ConfigHandler.COMMON.MOBS.GROTTOL.spawnConfig, MobCategory.MONSTER);
        LANTERN_BIOME_CHECKER = registerBiomeSpawn(EntityHandler.LANTERN.get(), ConfigHandler.COMMON.MOBS.LANTERN.spawnConfig, MobCategory.AMBIENT);
        NAGA_BIOME_CHECKER = registerBiomeSpawn(EntityHandler.NAGA.get(), ConfigHandler.COMMON.MOBS.NAGA.spawnConfig, MobCategory.MONSTER);
        BLUFF_BIOME_CHECKER = registerBiomeSpawn(EntityHandler.BLUFF.get(), ConfigHandler.COMMON.MOBS.BLUFF.spawnConfig, MobCategory.MONSTER);
        ELOKOSA_HOWLER_BIOME_CHECKER = registerBiomeSpawn(EntityHandler.ELOKOSA_HOWLER.get(), ConfigHandler.COMMON.MOBS.ELOKOSA.spawnConfig, MobCategory.MONSTER);
        biomeSpawnsRegistered = true;
    }


    private static void registerStructureBiomeRules() {
        if (structureBiomeRulesRegistered) return;

        BiomeModifications.create(MMCommon.resource("structure_biome_rules")).add(
                ModificationPhase.ADDITIONS,
                context -> true,
                (selectionContext, modificationContext) ->
                        StructureTypeHandler.addBiomeSpawns(selectionContext.getBiomeRegistryEntry())
        );
        structureBiomeRulesRegistered = true;
    }

    private static BiomeChecker registerBiomeSpawn(EntityType<?> entity, ConfigHandler.SpawnConfig config, MobCategory category) {
        BiomeChecker checker = new BiomeChecker(config.biomeConfig);
        if (config.spawnRate.get() > 0) {
            BiomeModifications.addSpawn(
                    context -> checker.isBiomeInConfig(context.getBiomeRegistryEntry()),
                    category,
                    entity,
                    config.spawnRate.get(),
                    config.minGroupSize.get(),
                    config.maxGroupSize.get()
            );
        }
        return checker;
    }
}
