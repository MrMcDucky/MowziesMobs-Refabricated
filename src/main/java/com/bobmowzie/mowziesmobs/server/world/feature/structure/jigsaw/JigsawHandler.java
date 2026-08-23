package com.bobmowzie.mowziesmobs.server.world.feature.structure.jigsaw;

import com.bobmowzie.mowziesmobs.MMCommon;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import io.github.fabricators_of_create.porting_lib.registry.DeferredHolder;
import io.github.fabricators_of_create.porting_lib.registry.DeferredRegister;

public class JigsawHandler {
    public static final DeferredRegister<StructurePoolElementType<?>> MM_STRUCTURE_POOLS = DeferredRegister.create(Registries.STRUCTURE_POOL_ELEMENT, MMCommon.MODID);

    public static final DeferredHolder<StructurePoolElementType<?>, StructurePoolElementType<MowziePoolElement>> MOWZIE_ELEMENT = MM_STRUCTURE_POOLS.register("mowzie_element", () -> () -> MowziePoolElement.CODEC);
    public static final DeferredHolder<StructurePoolElementType<?>, StructurePoolElementType<FallbackPoolElement>> FALLBACK_ELEMENT = MM_STRUCTURE_POOLS.register("fallback_element", () -> () -> FallbackPoolElement.CODEC);
}
