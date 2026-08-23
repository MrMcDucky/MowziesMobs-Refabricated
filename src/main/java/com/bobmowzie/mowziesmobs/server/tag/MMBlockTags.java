package com.bobmowzie.mowziesmobs.server.tag;

import com.bobmowzie.mowziesmobs.MMCommon;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

/** Runtime block tag keys; JSON data is bundled under src/generated/resources. */
public final class MMBlockTags {
    public static final TagKey<Block> GEOMANCY_USEABLE = key("geomancy_useable");
    public static final TagKey<Block> CAN_GROTTOL_DIG = key("can_grottol_dig");
    public static final TagKey<Block> GEOMANCY_TUNNELABLE = key("geomancy_tunnelable");

    private MMBlockTags() {}

    private static TagKey<Block> key(String path) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, path));
    }
}
