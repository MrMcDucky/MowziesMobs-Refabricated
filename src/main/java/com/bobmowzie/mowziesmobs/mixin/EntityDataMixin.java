package com.bobmowzie.mowziesmobs.mixin;

import com.bobmowzie.mowziesmobs.fabric.data.MowzieDataHolder;
import com.bobmowzie.mowziesmobs.server.capability.DataHandler;
import io.github.fabricators_of_create.porting_lib.core.util.INBTSerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.IdentityHashMap;
import java.util.Map;

@Mixin(Entity.class)
public abstract class EntityDataMixin implements MowzieDataHolder {
    @Unique private static final String MOWZIESMOBS_DATA = "MowziesMobsFabricData";
    @Unique private final Map<DataHandler.DataKey<?>, INBTSerializable<CompoundTag>> mowziesmobs$data = new IdentityHashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public <T extends INBTSerializable<CompoundTag>> T mowziesmobs$getData(DataHandler.DataKey<T> key) {
        return (T) mowziesmobs$data.computeIfAbsent(key, ignored -> key.factory().get());
    }

    @Inject(method = "saveWithoutId", at = @At("TAIL"))
    private void mowziesmobs$saveData(CompoundTag root, CallbackInfoReturnable<CompoundTag> cir) {
        Entity self = (Entity) (Object) this;
        if (mowziesmobs$data.isEmpty()) return;
        CompoundTag data = new CompoundTag();
        for (Map.Entry<DataHandler.DataKey<?>, INBTSerializable<CompoundTag>> entry : mowziesmobs$data.entrySet()) {
            if (entry.getKey().playerOnly() && !(self instanceof Player)) continue;
            data.put(entry.getKey().id(), entry.getValue().serializeNBT(self.level().registryAccess()));
        }
        if (!data.isEmpty()) root.put(MOWZIESMOBS_DATA, data);
    }

    @Inject(method = "load", at = @At("TAIL"))
    private void mowziesmobs$loadData(CompoundTag root, CallbackInfo ci) {
        Entity self = (Entity) (Object) this;
        if (!root.contains(MOWZIESMOBS_DATA, Tag.TAG_COMPOUND)) return;
        CompoundTag data = root.getCompound(MOWZIESMOBS_DATA);
        for (DataHandler.DataKey<?> key : DataHandler.ALL_KEYS) {
            if (key.playerOnly() && !(self instanceof Player)) continue;
            if (!data.contains(key.id(), Tag.TAG_COMPOUND)) continue;
            mowziesmobs$deserializeUnchecked(key, data.getCompound(key.id()), self);
        }
    }

    @Unique
    @SuppressWarnings({"unchecked", "rawtypes"})
    private void mowziesmobs$deserializeUnchecked(DataHandler.DataKey key, CompoundTag tag, Entity self) {
        INBTSerializable<CompoundTag> value = (INBTSerializable<CompoundTag>) mowziesmobs$getData(key);
        value.deserializeNBT(self.level().registryAccess(), tag);
    }
}
