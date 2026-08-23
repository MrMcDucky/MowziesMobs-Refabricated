package com.bobmowzie.mowziesmobs.server.tag;

import com.bobmowzie.mowziesmobs.MMCommon;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

/** Runtime tag keys. Datagen providers remain loader-specific and are excluded from the first Fabric build. */
public final class MMItemTags {
    public static final TagKey<Item> CAN_HIT_GROTTOL = key("can_hit_grottol");
    public static final TagKey<Item> HAND_WEAPONS = key("hand_weapons");

    private MMItemTags() {}

    private static TagKey<Item> key(String path) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MMCommon.MODID, path));
    }
}
