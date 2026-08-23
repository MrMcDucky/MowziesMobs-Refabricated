package com.ilexiconn.llibrary.server.event;

import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.world.entity.Entity;

/**
 * Lightweight loader-neutral animation event retained for source compatibility.
 * Mowzie's bundled LLibrary code has no external subscribers in this project,
 * so the Fabric port does not need a global loader event bus here.
 */
public class AnimationEvent<T extends Entity & IAnimatedEntity> {
    protected Animation animation;
    private final T entity;

    AnimationEvent(T entity, Animation animation) {
        this.entity = entity;
        this.animation = animation;
    }

    public T getEntity() { return this.entity; }
    public Animation getAnimation() { return this.animation; }

    public static class Start<T extends Entity & IAnimatedEntity> extends AnimationEvent<T> {
        private boolean canceled;

        public Start(T entity, Animation animation) { super(entity, animation); }
        public void setAnimation(Animation animation) { this.animation = animation; }
        public boolean isCanceled() { return canceled; }
        public void setCanceled(boolean canceled) { this.canceled = canceled; }
    }

    public static class Tick<T extends Entity & IAnimatedEntity> extends AnimationEvent<T> {
        protected int tick;
        public Tick(T entity, Animation animation, int tick) {
            super(entity, animation);
            this.tick = tick;
        }
        public int getTick() { return this.tick; }
    }
}
