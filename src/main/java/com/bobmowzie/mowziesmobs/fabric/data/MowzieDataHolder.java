package com.bobmowzie.mowziesmobs.fabric.data;

import com.bobmowzie.mowziesmobs.server.capability.DataHandler;
import io.github.fabricators_of_create.porting_lib.core.util.INBTSerializable;
import net.minecraft.nbt.CompoundTag;

public interface MowzieDataHolder {
    <T extends INBTSerializable<CompoundTag>> T mowziesmobs$getData(DataHandler.DataKey<T> key);
}
