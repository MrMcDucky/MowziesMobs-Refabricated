package com.ilexiconn.llibrary.server.animation;

import com.ilexiconn.llibrary.server.event.AnimationEvent;
import com.ilexiconn.llibrary.server.network.AnimationMessage;
import net.minecraft.world.entity.Entity;
import com.bobmowzie.mowziesmobs.server.message.NetworkHandler;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author iLexiconn
 * @since 1.0.0
 */
public enum AnimationHandler {
    INSTANCE;

    /**
     * Sends an animation packet to all clients, notifying them of a changed animation
     *
     * @param entity    the entity with an animation to be updated
     * @param animation the animation to be updated
     * @param <T>       the entity type
     */
    public <T extends Entity & IAnimatedEntity> void sendAnimationMessage(T entity, Animation animation) {
        if (entity.level().isClientSide) {
            return;
        }
        entity.setAnimation(animation);
        NetworkHandler.sendToPlayersTrackingEntityAndSelf(entity, new AnimationMessage(entity.getId(), ArrayUtils.indexOf(entity.getAnimations(), animation)));
    }

    /**
     * Updates all animations for a given entity
     *
     * @param entity the entity with an animation to be updated
     * @param <T>    the entity type
     */
    public <T extends Entity & IAnimatedEntity> void updateAnimations(T entity) {
        if (entity.getAnimation() == null) {
            entity.setAnimation(IAnimatedEntity.NO_ANIMATION);
        } else {
            if (entity.getAnimation() != IAnimatedEntity.NO_ANIMATION) {
                if (entity.getAnimationTick() == 0) {
                    AnimationEvent.Start<?> event = new AnimationEvent.Start<>(entity, entity.getAnimation());
                    if (!event.isCanceled()) {
                        this.sendAnimationMessage(entity, event.getAnimation());
                    }
                }
                if (entity.getAnimationTick() < entity.getAnimation().getDuration()) {
                    entity.setAnimationTick(entity.getAnimationTick() + 1);
                    // No project-local listeners use AnimationEvent.Tick; animation state itself is authoritative.
                }
                if (entity.getAnimationTick() == entity.getAnimation().getDuration()) {
                    if( ! entity.getAnimation().doesLoop()) {
                        entity.setAnimation(IAnimatedEntity.NO_ANIMATION);
                    }
                    entity.setAnimationTick(0);
                }
            }
        }
    }
}
