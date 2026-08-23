package com.bobmowzie.mowziesmobs.server.capability;

import com.bobmowzie.mowziesmobs.fabric.data.MowzieDataHolder;
import io.github.fabricators_of_create.porting_lib.core.util.INBTSerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

/**
 * Fabric replacement for NeoForge entity attachments.
 *
 * The rest of Mowzie's Mobs intentionally talks only to this class, so the
 * loader-specific storage can be replaced without touching ~130 call sites.
 */
public final class DataHandler {
    private DataHandler() {}

    public static final DataKey<FrozenData> FROZEN_DATA = new DataKey<>("frozen_data", FrozenData::new, false);
    public static final DataKey<LivingData> LIVING_DATA = new DataKey<>("living_data", LivingData::new, false);
    public static final DataKey<PlayerData> PLAYER_DATA = new DataKey<>("player_data", PlayerData::new, true);
    public static final DataKey<AbilityData> ABILITY_DATA = new DataKey<>("ability_data", AbilityData::new, false);

    public static final List<DataKey<?>> ALL_KEYS = List.of(FROZEN_DATA, LIVING_DATA, PLAYER_DATA, ABILITY_DATA);

    public static <T extends INBTSerializable<CompoundTag>> @NotNull T getData(@NotNull Entity entity, @NotNull DataKey<T> type) {
        if (type.playerOnly() && !(entity instanceof Player)) {
            throw new IllegalArgumentException("Cannot fetch player data for non-player entity of type [" + entity.getType() + "]");
        }
        return ((MowzieDataHolder) entity).mowziesmobs$getData(type);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void copyUnchecked(Entity from, Entity to, DataKey<?> key) {
        INBTSerializable<CompoundTag> source = (INBTSerializable<CompoundTag>) getData(from, (DataKey) key);
        INBTSerializable<CompoundTag> target = (INBTSerializable<CompoundTag>) getData(to, (DataKey) key);
        CompoundTag serialized = source.serializeNBT(from.level().registryAccess());
        target.deserializeNBT(to.level().registryAccess(), serialized.copy());
    }

    public record DataKey<T extends INBTSerializable<CompoundTag>>(String id, Supplier<T> factory, boolean playerOnly) {}
}
