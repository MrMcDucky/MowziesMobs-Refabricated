package com.bobmowzie.mowziesmobs.fabric.event;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

/**
 * Small loader-neutral compatibility event used by the Fabric port.
 * It mirrors the subset of NeoForge's LivingDamageEvent API Mowzie's Mobs uses.
 */
public abstract class LivingDamageEvent {
    private final LivingEntity entity;
    private final DamageSource source;
    private float newDamage;

    protected LivingDamageEvent(LivingEntity entity, DamageSource source, float newDamage) {
        this.entity = entity;
        this.source = source;
        this.newDamage = newDamage;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public DamageSource getSource() {
        return source;
    }

    public float getNewDamage() {
        return newDamage;
    }

    public void setNewDamage(float newDamage) {
        this.newDamage = newDamage;
    }

    public static final class Pre extends LivingDamageEvent {
        public Pre(LivingEntity entity, DamageSource source, float newDamage) {
            super(entity, source, newDamage);
        }
    }

    public static final class Post extends LivingDamageEvent {
        public Post(LivingEntity entity, DamageSource source, float newDamage) {
            super(entity, source, newDamage);
        }
    }
}
