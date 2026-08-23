package com.bobmowzie.mowziesmobs.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.decoration.BlockAttachedEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Targeted workaround for MC-252934 on the item frames bundled in the Monastery templates. */
@Mixin(BlockAttachedEntity.class)
public abstract class BlockAttachedEntityMixin {
    private static final String MONASTERY_ATTACHMENT_FIX = "MowziesMobsMonasteryAttachmentFix";

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    private void mowziesmobs$repairMonasteryStructureAttachment(CompoundTag tag, CallbackInfo ci) {
        if (!tag.getBoolean(MONASTERY_ATTACHMENT_FIX)) return;

        BlockPos pos = ((BlockAttachedEntity) (Object) this).blockPosition();
        tag.putInt("TileX", pos.getX());
        tag.putInt("TileY", pos.getY());
        tag.putInt("TileZ", pos.getZ());
        tag.remove(MONASTERY_ATTACHMENT_FIX);
    }
}
