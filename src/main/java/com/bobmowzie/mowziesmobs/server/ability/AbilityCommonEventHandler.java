package com.bobmowzie.mowziesmobs.server.ability;

import com.bobmowzie.mowziesmobs.server.capability.AbilityData;
import com.bobmowzie.mowziesmobs.server.capability.DataHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import com.bobmowzie.mowziesmobs.fabric.event.LivingDamageEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.living.LivingEvents;
import io.github.fabricators_of_create.porting_lib.entity.events.living.LivingFallEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.player.AttackEntityEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.player.PlayerInteractEvent;

public class AbilityCommonEventHandler {
    public void onPlayerInteract(PlayerInteractEvent.RightClickEmpty event) {
        Player player = event.getEntity();
        AbilityData data = DataHandler.getData(player, DataHandler.ABILITY_DATA);
        for (Ability<?> ability : data.getAbilities()) {
            if (ability instanceof PlayerAbility) {
                ((PlayerAbility) ability).onRightClickEmpty(event);
            }
        }
    }

    public void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        AbilityData data = DataHandler.getData(player, DataHandler.ABILITY_DATA);
        for (Ability<?> ability : data.getAbilities()) {
            if (ability instanceof PlayerAbility) {
                ((PlayerAbility) ability).onRightClickBlock(event);
            }
        }
    }

    public void onPlayerRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        AbilityData data = DataHandler.getData(player, DataHandler.ABILITY_DATA);
        for (Ability<?> ability : data.getAbilities()) {
            if (ability instanceof PlayerAbility) {
                ((PlayerAbility) ability).onRightClickWithItem(event);
            }
        }
    }

    public void onPlayerRightClickEntity(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        AbilityData data = DataHandler.getData(player, DataHandler.ABILITY_DATA);
        for (Ability<?> ability : data.getAbilities()) {
            if (ability instanceof PlayerAbility) {
                ((PlayerAbility) ability).onRightClickEntity(event);
            }
        }
    }

    public void onPlayerLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        Player player = event.getEntity();
        AbilityData data = DataHandler.getData(player, DataHandler.ABILITY_DATA);
        for (Ability<?> ability : data.getAbilities()) {
            if (ability instanceof PlayerAbility) {
                ((PlayerAbility) ability).onLeftClickEmpty(event);
            }
        }
    }

    public void onPlayerLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        AbilityData data = DataHandler.getData(player, DataHandler.ABILITY_DATA);
        for (Ability<?> ability : data.getAbilities()) {
            if (ability instanceof PlayerAbility) {
                ((PlayerAbility) ability).onLeftClickBlock(event);
            }
        }
    }

    public void onLeftClickEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        AbilityData data = DataHandler.getData(player, DataHandler.ABILITY_DATA);
        for (Ability<?> ability : data.getAbilities()) {
            if (ability instanceof PlayerAbility) {
                ((PlayerAbility) ability).onLeftClickEntity(event);
            }
        }
    }

    public void onTakeDamage(LivingDamageEvent.Post event) {
        LivingEntity player = event.getEntity();
        AbilityData data = DataHandler.getData(player, DataHandler.ABILITY_DATA);
        for (Ability<?> ability : data.getAbilities()) {
            ability.onTakeDamage(event);
        }
    }

    public void onJump(LivingEvents.LivingJumpEvent event) {
        LivingEntity player = event.getEntity();
        AbilityData data = DataHandler.getData(player, DataHandler.ABILITY_DATA);
        for (Ability<?> ability : data.getAbilities()) {
            if (ability instanceof PlayerAbility) {
                ((PlayerAbility) ability).onJump(event);
            }
        }
    }

    public void onFall(LivingFallEvent event) {
        LivingEntity player = event.getEntity();
        AbilityData data = DataHandler.getData(player, DataHandler.ABILITY_DATA);
        for (Ability<?> ability : data.getAbilities()) {
            if (ability instanceof PlayerAbility) {
                ((PlayerAbility) ability).onFall(event);
            }
        }
    }
}
