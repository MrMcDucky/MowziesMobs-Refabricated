package com.bobmowzie.mowziesmobs;

import com.bobmowzie.mowziesmobs.fabric.FabricCommonEvents;
import com.bobmowzie.mowziesmobs.server.message.NetworkHandler;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.server.ServerProxy;
import com.bobmowzie.mowziesmobs.server.advancement.AdvancementHandler;
import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import com.bobmowzie.mowziesmobs.server.block.entity.BlockEntityHandler;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerHandler;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.item.MaterialHandler;
import com.bobmowzie.mowziesmobs.server.loot.LootTableHandler;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import com.bobmowzie.mowziesmobs.server.potion.PotionTypeHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.bobmowzie.mowziesmobs.server.world.feature.structure.StructureTypeHandler;
import com.bobmowzie.mowziesmobs.server.world.feature.structure.jigsaw.JigsawHandler;
import com.bobmowzie.mowziesmobs.server.world.feature.structure.processor.ProcessorHandler;
import com.bobmowzie.mowziesmobs.server.world.spawn.SpawnHandler;
import io.github.fabricators_of_create.porting_lib.brewing.RegisterBrewingRecipesEvent;
import io.github.fabricators_of_create.porting_lib.config.ConfigRegistry;
import io.github.fabricators_of_create.porting_lib.config.ModConfig;
import io.github.fabricators_of_create.porting_lib.config.ModConfigEvent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Stream;

public final class MMCommon implements ModInitializer {
    public static final String MODID = "mowziesmobs";
    public static final Logger LOGGER = LogManager.getLogger();
    public static ServerProxy PROXY = new ServerProxy();

    @Override
    public void onInitialize() {
        BlockHandler.REG.register();
        EntityHandler.SERIALIZER_REG.register();
        EntityHandler.REG.register();
        MaterialHandler.MM_ARMOR_MATERIALS.register();
        ItemHandler.REG.register();
        MMSounds.REG.register();
        BlockEntityHandler.REG.register();
        ParticleHandler.REG.register();
        JigsawHandler.MM_STRUCTURE_POOLS.register();
        ProcessorHandler.MM_STRUCTURE_PROCESSORS.register();
        StructureTypeHandler.STRUCTURE_TYPE_REG.register();
        StructureTypeHandler.STRUCTURE_PIECE_TYPE_REG.register();
        ContainerHandler.REG.register();
        EffectHandler.REG.register();
        PotionTypeHandler.REG.register();
        RegisterBrewingRecipesEvent.EVENT.register(PotionTypeHandler::addMixes);
        LootTableHandler.LOOT_CONDITION_TYPE_REG.register();
        LootTableHandler.LOOT_FUNCTION_TYPE_REG.register();
        AdvancementHandler.MM_TRIGGERS.register();
        CreativeTabHandler.register();

        ConfigRegistry.registerConfig(MODID, ModConfig.Type.COMMON, ConfigHandler.COMMON_CONFIG);
        ConfigRegistry.loading(MODID).register(this::onModConfigEvent);
        ConfigRegistry.reloading(MODID).register(this::onModConfigEvent);

        EntityHandler.registerAttributes();
        SpawnHandler.registerSpawnPlacementTypes();
        DefaultItemComponentEvents.MODIFY.register(ItemHandler::modifyComponents);
        NetworkHandler.registerCommon();
        FabricCommonEvents.register();

        PROXY.init();
        ItemHandler.initializeDispenserBehaviors();
        BlockHandler.init();
    }

    public void onModConfigEvent(final ModConfigEvent event) {
        final ModConfig config = event.getConfig();
        // Rebake the configs when they change
        if (config.getSpec() == ConfigHandler.COMMON_CONFIG) {
            if (event instanceof ModConfigEvent.Loading) {
                SpawnHandler.registerBiomeSpawns();
            }
        	ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig.attackDamageValue = 
        			ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig.attackDamage.get().floatValue();
        	ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig.attackSpeedValue = 
        			ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig.attackSpeed.get().floatValue();
        	
        	ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SPEAR.toolConfig.attackDamageValue = 
        			ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SPEAR.toolConfig.attackDamage.get().floatValue();
        	ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SPEAR.toolConfig.attackSpeedValue = 
        			ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SPEAR.toolConfig.attackSpeed.get().floatValue();
        	
        	ConfigHandler.COMMON.TOOLS_AND_ABILITIES.NAGA_FANG_DAGGER.toolConfig.attackDamageValue = 
        			ConfigHandler.COMMON.TOOLS_AND_ABILITIES.NAGA_FANG_DAGGER.toolConfig.attackDamage.get().floatValue();
        	ConfigHandler.COMMON.TOOLS_AND_ABILITIES.NAGA_FANG_DAGGER.toolConfig.attackSpeedValue = 
        			ConfigHandler.COMMON.TOOLS_AND_ABILITIES.NAGA_FANG_DAGGER.toolConfig.attackSpeed.get().floatValue();     
        	
        	ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHREND_GAUNTLET.toolConfig.attackDamageValue =
        			ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHREND_GAUNTLET.toolConfig.attackDamage.get().floatValue();
        	ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHREND_GAUNTLET.toolConfig.attackSpeedValue =
        			ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHREND_GAUNTLET.toolConfig.attackSpeed.get().floatValue();
        	
        	ConfigHandler.COMMON.TOOLS_AND_ABILITIES.ICE_CRYSTAL.durabilityValue = 
        			ConfigHandler.COMMON.TOOLS_AND_ABILITIES.ICE_CRYSTAL.durability.get();
        	
        	ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHREND_GAUNTLET.durabilityValue =
        			ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHREND_GAUNTLET.durability.get();
        	
        	ConfigHandler.COMMON.TOOLS_AND_ABILITIES.WROUGHT_HELM.armorConfig.damageReductionMultiplierValue =
        			ConfigHandler.COMMON.TOOLS_AND_ABILITIES.WROUGHT_HELM.armorConfig.damageReductionMultiplier.get().floatValue();
        	ConfigHandler.COMMON.TOOLS_AND_ABILITIES.WROUGHT_HELM.armorConfig.toughnessMultiplierValue =
        			ConfigHandler.COMMON.TOOLS_AND_ABILITIES.WROUGHT_HELM.armorConfig.toughnessMultiplier.get().floatValue();
        	
        	ConfigHandler.COMMON.TOOLS_AND_ABILITIES.UMVUTHANA_MASK.armorConfig.damageReductionMultiplierValue =
        			ConfigHandler.COMMON.TOOLS_AND_ABILITIES.UMVUTHANA_MASK.armorConfig.damageReductionMultiplier.get().floatValue();
        	ConfigHandler.COMMON.TOOLS_AND_ABILITIES.UMVUTHANA_MASK.armorConfig.toughnessMultiplierValue =
        			ConfigHandler.COMMON.TOOLS_AND_ABILITIES.UMVUTHANA_MASK.armorConfig.toughnessMultiplier.get().floatValue();
        	
        	ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SOL_VISAGE.armorConfig.damageReductionMultiplierValue =
        			ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SOL_VISAGE.armorConfig.damageReductionMultiplier.get().floatValue();
        	ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SOL_VISAGE.armorConfig.toughnessMultiplierValue =
        			ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SOL_VISAGE.armorConfig.toughnessMultiplier.get().floatValue();

            ConfigHandler.COMMON.TOOLS_AND_ABILITIES.GEOMANCER_ARMOR.armorConfig.damageReductionMultiplierValue =
                    ConfigHandler.COMMON.TOOLS_AND_ABILITIES.GEOMANCER_ARMOR.armorConfig.damageReductionMultiplier.get().floatValue();
            ConfigHandler.COMMON.TOOLS_AND_ABILITIES.GEOMANCER_ARMOR.armorConfig.toughnessMultiplierValue =
                    ConfigHandler.COMMON.TOOLS_AND_ABILITIES.GEOMANCER_ARMOR.armorConfig.toughnessMultiplier.get().floatValue();
        }
    }

    public static ResourceLocation resource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static Stream<EntityType<? extends LivingEntity>> getLivingEntityTypes() {
        // Logic to collect living entity types is from EntityAttributeModificationEvent
        return BuiltInRegistries.ENTITY_TYPE.stream().filter(DefaultAttributes::hasSupplier).map(entityType -> (EntityType<? extends LivingEntity>) entityType);
    }
}
