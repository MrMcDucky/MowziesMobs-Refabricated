package com.bobmowzie.mowziesmobs.fabric;

import com.bobmowzie.mowziesmobs.fabric.event.LivingDamageEvent;
import com.bobmowzie.mowziesmobs.server.ServerEventHandler;
import com.bobmowzie.mowziesmobs.server.ability.AbilityCommonEventHandler;
import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import io.github.fabricators_of_create.porting_lib.entity.events.EntityJoinLevelEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.EntityMountEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.living.LivingDeathEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.living.LivingEntityUseItemEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.living.LivingFallEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.living.LivingHurtEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.living.LivingEvents;
import io.github.fabricators_of_create.porting_lib.entity.events.living.MobEffectEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.player.AttackEntityEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.player.CriticalHitEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.player.PlayerInteractEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.tick.EntityTickEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.tick.PlayerTickEvent;
import io.github.fabricators_of_create.porting_lib.level.events.BlockEvent;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.registry.FuelRegistry;

/** Registers the common/server callbacks that NeoForge previously discovered through @SubscribeEvent. */
public final class FabricCommonEvents {
    private static boolean registered;

    private FabricCommonEvents() {}

    public static void register() {
        if (registered) return;
        registered = true;

        ServerEventHandler server = new ServerEventHandler();
        AbilityCommonEventHandler abilities = new AbilityCommonEventHandler();

        EntityJoinLevelEvent.EVENT.register(server::onJoinWorld);
        EntityTickEvent.Post.EVENT.register(server::onLivingTick);
        PlayerTickEvent.Post.EVENT.register(server::onPlayerTick);
        ServerEntityEvents.EQUIPMENT_CHANGE.register((entity, slot, previousStack, currentStack) -> server.onEquipmentChanged(entity, slot));

        MobEffectEvent.Applicable.EVENT.register(server::onPotionEffectApplicable);
        MobEffectEvent.Added.EVENT.register(server::onAddPotionEffect);
        MobEffectEvent.Remove.EVENT.register(server::onRemovePotionEffect);
        MobEffectEvent.Expired.EVENT.register(server::onPotionEffectExpire);

        LivingFallEvent.EVENT.register(event -> {
            server.onLivingFall(event);
            abilities.onFall(event);
        });
        LivingEntityUseItemEvent.Start.EVENT.register(server::onUseItem);
        LivingDeathEvent.EVENT.register(server::onLivingDeath);

        BlockEvent.EntityPlaceEvent.EVENT.register(server::onPlaceBlock);
        BlockEvent.BreakEvent.EVENT.register(server::onBreakBlock);

        PlayerInteractEvent.RightClickEmpty.EVENT.register(event -> {
            server.onPlayerInteract(event);
            abilities.onPlayerInteract(event);
        });
        PlayerInteractEvent.RightClickBlock.EVENT.register(event -> {
            server.onPlayerInteract(event);
            abilities.onPlayerRightClickBlock(event);
        });
        PlayerInteractEvent.RightClickItem.EVENT.register(event -> {
            server.onFillBucket(event);
            server.onPlayerInteract(event);
            abilities.onPlayerRightClickItem(event);
        });
        PlayerInteractEvent.EntityInteract.EVENT.register(event -> {
            server.onPlayerInteract(event);
            abilities.onPlayerRightClickEntity(event);
        });
        PlayerInteractEvent.LeftClickEmpty.EVENT.register(event -> {
            server.onPlayerLeftClick(event);
            abilities.onPlayerLeftClickEmpty(event);
        });
        PlayerInteractEvent.LeftClickBlock.EVENT.register(event -> {
            server.onPlayerLeftClick(event);
            abilities.onPlayerLeftClickBlock(event);
        });

        AttackEntityEvent.EVENT.register(event -> {
            server.onPlayerAttack(event);
            abilities.onLeftClickEntity(event);
        });
        CriticalHitEvent.EVENT.register(server::checkCritEvent);
        // Porting Lib fires EntityMountEvent for both mounting and dismounting.
        // Mowzie mobs are intentionally non-mountable, but their forced freeze mount
        // must still be allowed to dismount when the frozen effect ends. Forward only
        // actual mount attempts to the common handler; never cancel dismounts.
        EntityMountEvent.EVENT.register(event -> {
            if (event.isMounting()) {
                server.onRideEntity(event);
            }
        });
        LivingEvents.LivingJumpEvent.EVENT.register(event -> {
            server.onLivingJump(event);
            abilities.onJump(event);
        });

        // Porting Lib's LivingHurtEvent is mutable and runs from actuallyHurt.
        // It is the closest Fabric-side match for NeoForge's LivingDamageEvent.Pre.
        LivingHurtEvent.EVENT.register(event -> {
            LivingDamageEvent.Pre compat = new LivingDamageEvent.Pre(event.getEntity(), event.getSource(), event.getAmount());
            server.onLivingHurtPre(compat);
            event.setAmount(compat.getNewDamage());
        });

        // Fabric exposes a post-damage callback on the logical server. Fatal damage uses
        // ALLOW_DEATH because AFTER_DAMAGE is intentionally skipped when the hit kills.
        ServerLivingEntityEvents.AFTER_DAMAGE.register((entity, source, baseDamageTaken, damageTaken, blocked) -> {
            LivingDamageEvent.Post compat = new LivingDamageEvent.Post(entity, source, damageTaken);
            server.onLivingHurtPost(compat);
            server.onLivingDamage(compat);
            abilities.onTakeDamage(compat);
        });
        ServerLivingEntityEvents.ALLOW_DEATH.register((entity, source, damageAmount) -> {
            LivingDamageEvent.Post compat = new LivingDamageEvent.Post(entity, source, damageAmount);
            server.onLivingHurtPost(compat);
            server.onLivingDamage(compat);
            abilities.onTakeDamage(compat);
            return true;
        });

        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> server.onPlayerRespawn(newPlayer));

        // Minecraft 1.21.1 still uses FuelRegistry.INSTANCE (FuelRegistryEvents is 1.21.2+).
        FuelRegistry.INSTANCE.add(BlockHandler.CLAWED_LOG.get().asItem(), 300);
        FuelRegistry.INSTANCE.add(BlockHandler.PAINTED_ACACIA.get().asItem(), 300);
        FuelRegistry.INSTANCE.add(BlockHandler.PAINTED_ACACIA_SLAB.get().asItem(), 150);
        FuelRegistry.INSTANCE.add(BlockHandler.THATCH.get().asItem(), 100);
    }
}
