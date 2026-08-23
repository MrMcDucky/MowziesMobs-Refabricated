package com.bobmowzie.mowziesmobs.client.particle;

import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

/** Client-only particle factory wiring. ParticleHandler itself remains safe to initialize from common code. */
public final class ClientParticleHandler {
    private ClientParticleHandler() {}

    public static void registerParticles() {
        ParticleFactoryRegistry event = ParticleFactoryRegistry.getInstance();
        event.register(ParticleHandler.SPARKLE.get(), ParticleSparkle.Provider::new);
        event.register(ParticleHandler.VANILLA_CLOUD_EXTENDED.get(), ParticleVanillaCloudExtended.Provider::new);
        event.register(ParticleHandler.SNOWFLAKE.get(), ParticleSnowFlake.Provider::new);
        event.register(ParticleHandler.CLOUD.get(), ParticleCloud.Provider::new);
        event.register(ParticleHandler.ORB.get(), ParticleOrb.Provider::new);
        event.register(ParticleHandler.RING.get(), ParticleRing.Provider::new);

        event.register(ParticleHandler.RING2.get(), AdvancedParticleBase.Factory::new);
        event.register(ParticleHandler.RING_BIG.get(), AdvancedParticleBase.Factory::new);
        event.register(ParticleHandler.PIXEL.get(), AdvancedParticleBase.Factory::new);
        event.register(ParticleHandler.ORB2.get(), AdvancedParticleBase.Factory::new);
        event.register(ParticleHandler.EYE.get(), AdvancedParticleBase.Factory::new);
        event.register(ParticleHandler.BUBBLE.get(), AdvancedParticleBase.Factory::new);
        event.register(ParticleHandler.SUN.get(), AdvancedParticleBase.Factory::new);
        event.register(ParticleHandler.SUN_NOVA.get(), AdvancedParticleBase.Factory::new);
        event.register(ParticleHandler.FLARE.get(), AdvancedParticleBase.Factory::new);
        event.register(ParticleHandler.FLARE_RADIAL.get(), AdvancedParticleBase.Factory::new);
        event.register(ParticleHandler.BURST_IN.get(), AdvancedParticleBase.Factory::new);
        event.register(ParticleHandler.BURST_MESSY.get(), AdvancedParticleBase.Factory::new);
        event.register(ParticleHandler.RING_SPARKS.get(), AdvancedParticleBase.Factory::new);
        event.register(ParticleHandler.BURST_OUT.get(), AdvancedParticleBase.Factory::new);
        event.register(ParticleHandler.GLOW.get(), AdvancedParticleBase.Factory::new);
        event.register(ParticleHandler.ARROW_HEAD.get(), AdvancedParticleBase.Factory::new);
        event.register(ParticleHandler.LEAF.get(), AdvancedParticleBase.Factory::new);
        event.register(ParticleHandler.TERRAIN.get(), AdvancedTerrainParticle.Factory::new);
        event.register(ParticleHandler.MOON_FULL.get(), AdvancedParticleBase.Factory::new);
        event.register(ParticleHandler.MOON_GIBBOUS.get(), AdvancedParticleBase.Factory::new);
        event.register(ParticleHandler.MOON_HALF.get(), AdvancedParticleBase.Factory::new);
        event.register(ParticleHandler.MOON_CRESCENT.get(), AdvancedParticleBase.Factory::new);
        event.register(ParticleHandler.MOON_NEW.get(), AdvancedParticleBase.Factory::new);

        event.register(ParticleHandler.STRIX_FOOTPRINT.get(), ParticleDecal.Provider::new);
        event.register(ParticleHandler.GROUND_CRACK.get(), ParticleDecal.Provider::new);
        event.register(ParticleHandler.PLAYER_FOOTPRINT.get(), ParticleDecal.Provider::new);

        event.register(ParticleHandler.RIBBON_FLAT.get(), ParticleRibbon.Provider::new);
        event.register(ParticleHandler.RIBBON_STREAKS.get(), ParticleRibbon.Provider::new);
        event.register(ParticleHandler.RIBBON_GLOW.get(), ParticleRibbon.Provider::new);
        event.register(ParticleHandler.RIBBON_SQUIGGLE.get(), ParticleRibbon.Provider::new);
    }
}
