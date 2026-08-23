package com.bobmowzie.mowziesmobs.server.tag;

import com.bobmowzie.mowziesmobs.MMCommon;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public final class MMBiomeTags {
    public static final TagKey<Biome> HAS_MOWZIE_STRUCTURE = key("has_structure/has_mowzie_structure");
    public static final TagKey<Biome> IS_MAGICAL = key("is_magical");

    private MMBiomeTags() {}

    private static TagKey<Biome> key(String path) {
        return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, path));
    }
}
