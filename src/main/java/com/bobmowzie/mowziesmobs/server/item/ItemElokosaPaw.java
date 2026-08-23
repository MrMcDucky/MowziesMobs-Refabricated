package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.elokosa.PawType;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import io.github.fabricators_of_create.porting_lib.registry.DeferredHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class ItemElokosaPaw extends Item {
    private final PawType pawType;

    public ItemElokosaPaw(PawType pawType, Properties properties) {
        super(properties.stacksTo(1).durability(3));
        this.pawType = pawType;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemstack = player.getItemInHand(usedHand);
        player.playSound(MMSounds.ENTITY_ELOKOSA_PAW.get(), 1, 1.15f - 0.06f * pawType.ordinal());
        int cooldown = ConfigHandler.COMMON.TOOLS_AND_ABILITIES.ELOKOSA_PAW.cooldown.getAsInt();
        if (!player.hasInfiniteMaterials()) {
            for (DeferredHolder<Item, ItemElokosaPaw> item : ItemHandler.ELOKOSA_PAWS) {
                player.getCooldowns().addCooldown(item.get(), cooldown);
            }
        }
        itemstack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(usedHand));

        // Client visuals are routed through the sided proxy. The dedicated server's
        // ServerProxy implementation is a no-op and therefore never links Minecraft
        // client particle classes while resolving ItemElokosaPaw.use().
        MMCommon.PROXY.spawnElokosaPawParticles(level, player, usedHand, pawType);

        if (!level.isClientSide()) {
            float radius = 10;
            List<LivingEntity> entitiesNearby = level.getEntitiesOfClass(
                    LivingEntity.class,
                    player.getBoundingBox().inflate(radius, radius, radius),
                    e -> e.distanceToSqr(player) <= radius * radius && e != player
            );
            int effectDuration = ConfigHandler.COMMON.TOOLS_AND_ABILITIES.ELOKOSA_PAW.effectDuration.getAsInt();
            for (LivingEntity entity : entitiesNearby) {
                entity.addEffect(new MobEffectInstance(pawType.getPotion(), effectDuration));
            }
        }
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip, flagIn);
        tooltip.add(Component.translatable(getDescriptionId() + ".text.0").setStyle(ItemHandler.TOOLTIP_STYLE));
        if (pawType == PawType.CRESCENT || pawType == PawType.NEW) {
            tooltip.add(Component.translatable(getDescriptionId() + ".text.1").setStyle(ItemHandler.TOOLTIP_STYLE));
        }
        if (pawType == PawType.NEW) {
            tooltip.add(Component.translatable(getDescriptionId() + ".text.2").setStyle(ItemHandler.TOOLTIP_STYLE));
        }
    }
}
