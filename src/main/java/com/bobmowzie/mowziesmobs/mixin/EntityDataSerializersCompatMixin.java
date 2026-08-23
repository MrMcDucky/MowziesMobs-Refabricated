package com.bobmowzie.mowziesmobs.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.fabricators_of_create.porting_lib.entity.data.PortingLibEntityDataSerializers;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.util.CrudeIncrementalIntIdentityHashBiMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Compatibility shim for Porting Lib 3.1.0+1.21.1's entity-data-serializer module.
 * The vendored 1.21.1 source ships its mixin config as
 * porting_lib_entity_data_serializers.json instead of the .mixins.json filename
 * its build expander recognizes, so the intended serializer lookup hooks are not
 * installed in production. Reproduce those two tiny hooks here.
 */
@Mixin(EntityDataSerializers.class)
public abstract class EntityDataSerializersCompatMixin {
    @WrapOperation(
            method = "getSerializer",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/CrudeIncrementalIntIdentityHashBiMap;byId(I)Ljava/lang/Object;")
    )
    private static Object mowziesmobs$getSerializer(CrudeIncrementalIntIdentityHashBiMap<EntityDataSerializer<?>> instance,
                                                     int id, Operation<Object> original) {
        return PortingLibEntityDataSerializers.getSerializer(id, instance, original);
    }

    @WrapOperation(
            method = "getSerializedId",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/CrudeIncrementalIntIdentityHashBiMap;getId(Ljava/lang/Object;)I")
    )
    private static int mowziesmobs$getSerializedId(CrudeIncrementalIntIdentityHashBiMap<EntityDataSerializer<?>> instance,
                                                    Object value, Operation<Integer> original) {
        return PortingLibEntityDataSerializers.getSerializerId((EntityDataSerializer<?>) value, instance, original);
    }
}
