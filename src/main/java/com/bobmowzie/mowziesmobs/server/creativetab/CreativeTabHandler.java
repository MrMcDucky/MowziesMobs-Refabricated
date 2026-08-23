package com.bobmowzie.mowziesmobs.server.creativetab;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import io.github.fabricators_of_create.porting_lib.registry.DeferredHolder;
import io.github.fabricators_of_create.porting_lib.registry.DeferredRegister;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;

public class CreativeTabHandler {
    public static final DeferredRegister<CreativeModeTab> REG = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MMCommon.MODID);

    public static DeferredHolder<CreativeModeTab, CreativeModeTab> CREATIVE_TAB = REG.register("mowziesmobs_tab", () -> FabricItemGroup.builder()
            .icon(() -> ItemHandler.LOGO.get().getDefaultInstance())
            .title(Component.translatable("itemGroup.mowziesmobs.creativeTab"))
            .displayItems((displayParams, output) -> {
                for (DeferredHolder<Item, ? extends Item> item : ItemHandler.REG.getEntries()) {
                    if (item == ItemHandler.LOGO) continue;
                    output.accept(item.get());
                }
            })
            .build());

    public static void register() {
        REG.register();
    }
}
