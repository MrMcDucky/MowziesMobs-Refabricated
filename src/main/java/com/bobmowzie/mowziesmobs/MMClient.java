package com.bobmowzie.mowziesmobs;

import com.bobmowzie.mowziesmobs.client.ClientEventBusSubscriber;
import com.bobmowzie.mowziesmobs.client.ClientLayerRegistry;
import com.bobmowzie.mowziesmobs.client.ClientProxy;
import com.bobmowzie.mowziesmobs.client.MMModels;
import com.bobmowzie.mowziesmobs.client.model.LayerHandler;
import com.bobmowzie.mowziesmobs.client.model.tools.MowzieModelFactory;
import com.bobmowzie.mowziesmobs.client.particle.ClientParticleHandler;
import com.bobmowzie.mowziesmobs.client.render.entity.FrozenRenderHandler;
import com.bobmowzie.mowziesmobs.client.render.item.RenderWroughtHelmArmor;
import com.bobmowzie.mowziesmobs.fabric.FabricClientEvents;
import com.bobmowzie.mowziesmobs.server.ability.AbilityClientEventHandler;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.item.*;
import com.bobmowzie.mowziesmobs.client.network.ClientNetworkHandler;
import io.github.fabricators_of_create.porting_lib.client.armor.ArmorRendererRegistry;
import io.github.fabricators_of_create.porting_lib.client_events.event.client.EntityRenderersEvent;
import io.github.fabricators_of_create.porting_lib.client_events.event.client.RenderHandEvent;
import io.github.fabricators_of_create.porting_lib.config.ConfigRegistry;
import io.github.fabricators_of_create.porting_lib.config.ModConfig;
import io.github.fabricators_of_create.porting_lib.event.client.RenderFrameEvent;
import io.github.fabricators_of_create.porting_lib.models.events.client.ModelEvent;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import software.bernie.geckolib.util.GeckoLibUtil;

public final class MMClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MMCommon.PROXY = new ClientProxy();
        MMCommon.PROXY.init();

        ConfigRegistry.registerConfig(MMCommon.MODID, ModConfig.Type.CLIENT, ConfigHandler.CLIENT_CONFIG);

        GeckoLibUtil.addCustomBakedModelFactory(MMCommon.MODID, new MowzieModelFactory());
        ClientEventBusSubscriber.registerRenderers();
        ClientEventBusSubscriber.registerMenuScreens();
        LayerHandler.registerLayers();
        ArmorRendererRegistry.register(new RenderWroughtHelmArmor(), ItemHandler.WROUGHT_HELMET.get());
        ClientParticleHandler.registerParticles();

        ModelEvent.RegisterAdditional.EVENT.register(ClientEventBusSubscriber::onRegisterModels);
        ModelEvent.ModifyBakingResult.EVENT.register(MMModels::onModelBakeEvent);
        EntityRenderersEvent.AddLayers.EVENT.register(ClientLayerRegistry::onAddLayers);
        RenderHandEvent.EVENT.register(FrozenRenderHandler::onRenderHand);
        RenderFrameEvent.POST.register(AbilityClientEventHandler::onRenderTick);

        registerItemProperties();
        ClientNetworkHandler.register();
        FabricClientEvents.register();
    }

    private static void registerItemProperties() {
        ItemPropertyFunction pulling = ItemProperties.getProperty(Items.BOW.getDefaultInstance(), ResourceLocation.withDefaultNamespace("pulling"));
        if (pulling instanceof ClampedItemPropertyFunction clampedPulling) {
            ItemProperties.register(ItemHandler.BLOWGUN.get().asItem(), ResourceLocation.withDefaultNamespace("pulling"), clampedPulling);
        }
    }


}
