package com.bobmowzie.mowziesmobs.server.world;

import com.bobmowzie.mowziesmobs.MMCommon;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.StructureSet;

/** Runtime structure-set keys. The actual structure-set JSON is bundled with the mod. */
public final class StructureSetKeys {
    public static final ResourceKey<StructureSet> WROUGHT_CHAMBERS = key("wrought_chambers");
    public static final ResourceKey<StructureSet> UMVUTHANA_GROVES = key("umvuthana_groves");
    public static final ResourceKey<StructureSet> FROSTMAWS = key("frostmaw_spawns");
    public static final ResourceKey<StructureSet> MONASTERIES = key("monasteries");

    private StructureSetKeys() {}

    private static ResourceKey<StructureSet> key(String name) {
        return ResourceKey.create(Registries.STRUCTURE_SET, ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, name));
    }
}
