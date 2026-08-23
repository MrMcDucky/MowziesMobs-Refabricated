package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.client.render.block.SculptorBlockMarking;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.client.sound.*;
import com.bobmowzie.mowziesmobs.server.ServerProxy;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySolarBeam;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySunstrike;
import com.bobmowzie.mowziesmobs.server.entity.naga.EntityNaga;
import com.bobmowzie.mowziesmobs.server.entity.elokosa.PawType;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.CameraType;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TerrainParticle;
import io.github.fabricators_of_create.porting_lib.models.extensions.TerrainParticleExtensions;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ClientProxy extends ServerProxy {
    private static final List<SunblockSound> sunblockSounds = new ArrayList<>();
    public static final Map<UUID, ResourceLocation> bossBarRegistryNames = new HashMap<>();

    public static final Long2ObjectMap<SculptorBlockMarking> sculptorMarkedBlocks = new Long2ObjectOpenHashMap<>();

    private Entity referencedMob = null;


    @Override
    public void sendToServer(CustomPacketPayload payload) {
        ClientPlayNetworking.send(payload);
    }

    @Override
    public void playSunstrikeSound(EntitySunstrike strike) {
        Minecraft.getInstance().getSoundManager().play(new SunstrikeSound(strike));
    }

    @Override
    public void playIceBreathSound(Entity entity) {
        Minecraft.getInstance().getSoundManager().play(new IceBreathSound(entity));
    }

    @Override
    public void playBoulderChargeSound(LivingEntity player) {
        Minecraft.getInstance().getSoundManager().play(new SpawnBoulderChargeSound(player));
    }

    @Override
    public void playNagaSwoopSound(EntityNaga naga) {
        Minecraft.getInstance().getSoundManager().play(new NagaSwoopSound(naga));
    }

    @Override
    public void playBlackPinkSound(AbstractMinecart entity) {
        Minecraft.getInstance().getSoundManager().play(new BlackPinkSound(entity));
    }

    @Override
    public void playSunblockSound(LivingEntity entity) {
        if (ConfigHandler.CLIENT.doUmvuthanaCraneHealSound.get()) {
            sunblockSounds.removeIf(e -> e == null || e.isStopped());
            if (sunblockSounds.size() < 10) {
                SunblockSound sunblockSound = new SunblockSound(entity);
                sunblockSounds.add(sunblockSound);
                try {
                    Minecraft.getInstance().getSoundManager().play(sunblockSound);
                } catch (ConcurrentModificationException ignored) {

                }
            }
        }
    }

    @Override
    public void playSolarBeamSound(EntitySolarBeam entity) {
        Minecraft.getInstance().getSoundManager().play(new SolarBeamSound(entity, false));
        Minecraft.getInstance().getSoundManager().play(new SolarBeamSound(entity, true));
    }

    public void playGeomancyRumbleSound(IGeomancyRumbler rumbler) {
        Minecraft.getInstance().getSoundManager().play(new EarthRumbleLoopSound(rumbler));
    }

    @Override
    public void spawnElokosaPawParticles(Level level, Player player, InteractionHand usedHand, PawType pawType) {
        float ringScale = 100f;
        AdvancedParticleBase.spawnParticle(level, ParticleHandler.BURST_MESSY, player.getX(), player.getY() + player.getBbHeight()/2f, player.getZ(), 0, 0, 0, false, 0, Math.PI/2f, 0, 0, 5F, 0.70,0.55,0.99, 1, 1, 20, true, false, new ParticleComponent[]{
                new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, new ParticleComponent.Expression(t -> (float) Math.pow(t, 0.6) * ringScale * 1.3f), false),
                new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, new ParticleComponent.Expression(t -> 1f - (float) Math.pow(t, 0.5)), false),
        });
        AdvancedParticleBase.spawnParticle(level, ParticleHandler.RING_BIG, player.getX(), player.getY() + player.getBbHeight()/2f, player.getZ(), 0, 0, 0, false, 0, Math.PI/2f, 0, 0, 5F, 0.85,0.65,0.99, 1, 1, 15, true, false, new ParticleComponent[]{
                new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, new ParticleComponent.Expression(t -> (float) Math.pow(t, 0.6) * ringScale), false),
                new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, new ParticleComponent.Expression(t -> 1f - (float) Math.pow(t, 0.5)), false),
        });
        float glowScale = 130f;
        AdvancedParticleBase.spawnParticle(level, ParticleHandler.GLOW, player.getX(), player.getY() + player.getBbHeight()/2f, player.getZ(), 0, 0, 0, true, 0, 0, 0, 0, 5F, 0.65,0.45,0.95, 1, 1, 20, true, false, new ParticleComponent[]{
                new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, new ParticleComponent.Expression(t -> (float) Math.pow(t, 0.6) * glowScale), false),
                new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, new ParticleComponent.Expression(t -> 1f - (float) Math.pow(t, 0.5)), false),
        });

        Vec3 moonPos;
        float moonSize;
        if (Minecraft.getInstance().player == player && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON) {
            boolean usingMainHand = usedHand == InteractionHand.MAIN_HAND;
            boolean isRightHanded = player.getMainArm() == HumanoidArm.RIGHT;
            boolean usedRightHand = usingMainHand == isRightHanded;
            float sidewaysOffset = 0.76f;
            Vec3 playerLeftAngle = player.getUpVector(0).cross(player.getLookAngle());
            moonPos = player.getEyePosition().add(player.getLookAngle()).add(player.getUpVector(0).scale(0.2f)).add(playerLeftAngle.scale(usedRightHand ? -sidewaysOffset : sidewaysOffset));
            moonSize = 4.3f;
            AdvancedParticleBase.spawnParticle(level, pawType.getParticleType(), moonPos.x, moonPos.y, moonPos.z, 0, 0, 0, true, 0, 0, 0, 0, 5F, 1, 1, 1, 1, 1, 25, true, false, new ParticleComponent[]{
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, new ParticleComponent.KeyTrack(
                            new float[] {0, 0, 1f, 1f, 0},
                            new float[] {0, 0.05f, 0.15f, 0.5f, 1}
                    ), false),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, new ParticleComponent.Expression(t -> ((1f - (float) Math.pow(t, 0.6))*((float) Math.pow(t, 0.5))*7f + 1f) * moonSize), false),
                    new ParticleComponent.ScreenSpace(usedRightHand ? sidewaysOffset : -sidewaysOffset, 1.2f, -0.5f)
            });
            AdvancedParticleBase.spawnParticle(level, ParticleHandler.GLOW, moonPos.x, moonPos.y, moonPos.z, 0, 0, 0, true, 0, 0, 0, 0, 5F, 0.4f, 0.576, 1, 1, 1, 25, true, false, new ParticleComponent[]{
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, new ParticleComponent.KeyTrack(
                            new float[] {0, 0, 0.3f, 0.3f, 0},
                            new float[] {0, 0.05f, 0.15f, 0.5f, 1}
                    ), false),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, new ParticleComponent.Expression(t -> ((1f - (float) Math.pow(t, 0.8))*((float) Math.pow(t, 0.5))*7f + 1f) * moonSize * 0.8f), false),
                    new ParticleComponent.ScreenSpace(usedRightHand ? sidewaysOffset : -sidewaysOffset, 1.2f, -0.5f)
            });
        } else {
            moonSize = 5f;
            moonPos = new Vec3(player.getX(), player.getY() + player.getBbHeight() + 0.65f, player.getZ());
            AdvancedParticleBase.spawnParticle(level, pawType.getParticleType(), moonPos.x, moonPos.y, moonPos.z, 0, 0, 0, true, 0, 0, 0, 0, 5F, 1, 1, 1, 1, 1, 25, true, false, new ParticleComponent[]{
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, new ParticleComponent.KeyTrack(
                            new float[] {0, 0, 1f, 1f, 0},
                            new float[] {0, 0.05f, 0.15f, 0.5f, 1}
                    ), false),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, new ParticleComponent.Expression(t -> ((1f - (float) Math.pow(t, 0.6))*((float) Math.pow(t, 0.5))*7f + 1f) * moonSize), false),
            });
            AdvancedParticleBase.spawnParticle(level, ParticleHandler.GLOW, moonPos.x, moonPos.y, moonPos.z, 0, 0, 0, true, 0, 0, 0, 0, 5F, 0.4f, 0.576, 1, 1, 1, 25, true, false, new ParticleComponent[]{
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, new ParticleComponent.KeyTrack(
                            new float[] {0, 0, 0.3f, 0.3f, 0},
                            new float[] {0, 0.05f, 0.15f, 0.5f, 1}
                    ), false),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, new ParticleComponent.Expression(t -> ((1f - (float) Math.pow(t, 0.8))*((float) Math.pow(t, 0.5))*7f + 1f) * moonSize * 0.8f), false),
            });
        }

        for (int i = 0; i < 60; i++) {
            Vec3 vel = new Vec3(0.15 + player.getRandom().nextFloat() * 0.4f, 0, 0).yRot(player.getRandom().nextFloat() * Mth.TWO_PI).xRot(player.getRandom().nextFloat() * Mth.TWO_PI);
            AdvancedParticleBase.spawnParticle(level, ParticleHandler.PIXEL, moonPos.x + vel.x, moonPos.y + vel.y, moonPos.z + vel.z, vel.x, vel.y, vel.z, true, 0, 0, 0, 0, 5F, 0.85,0.65,0.99, 1, 0.9, 10 + player.getRandom().nextInt(10), true, true, new ParticleComponent[]{
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, new ParticleComponent.Expression(t -> (1f - (float) Math.pow(t, 2)) * 2.5f), false),
                    new ParticleComponent.ForceOverTime(new Vec3(0, 0.003, 0)),
                    new ParticleComponent.Vortex(new Vec3(0, 1, 0), new Vec3(player.getX(), player.getY() + player.getBbHeight()/2f, player.getZ()), ParticleComponent.KeyTrack.startAndEnd(player.getRandom().nextFloat() * 0.15f, 0f)),
                    new ParticleComponent.CurlNoise(0.025f, 4f),
            });
        }

        float radius = 10;
        List<LivingEntity> entitiesNearby = level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(radius, radius, radius), e -> e.distanceToSqr(player) <= radius * radius && e != player);
        for (LivingEntity entity : entitiesNearby) {
            for (int i = 0; i < 30; i++) {
                Vec3 vel = new Vec3(0.05 + entity.getRandom().nextFloat() * 0.1f, 0, 0).yRot(entity.getRandom().nextFloat() * Mth.TWO_PI).xRot(entity.getRandom().nextFloat() * Mth.TWO_PI);
                AdvancedParticleBase.spawnParticle(level, ParticleHandler.PIXEL, entity.getX(), entity.getY() + entity.getBbHeight()/2f, entity.getZ(), vel.x, vel.y, vel.z, true, 0, 0, 0, 0, 5F, 0.85,0.65,0.99, 1, 0.9, 10 + player.getRandom().nextInt(10), true, true, new ParticleComponent[]{
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, new ParticleComponent.Expression(t -> (1f - (float) Math.pow(t, 2)) * 2.5f), false),
                        new ParticleComponent.ForceOverTime(new Vec3(0, 0.003, 0)),
                        new ParticleComponent.Vortex(new Vec3(0, 1, 0), new Vec3(entity.getX(), entity.getY() + entity.getBbHeight()/2f, entity.getZ()), ParticleComponent.KeyTrack.startAndEnd(entity.getRandom().nextFloat() * 0.1f, 0f)),
                        new ParticleComponent.CurlNoise(0.025f, 4f),
                });
            }
            AdvancedParticleBase.spawnParticle(level, ParticleHandler.BURST_OUT, entity.getX(), entity.getY() + entity.getBbHeight()/2f, entity.getZ(), 0, 0, 0, true, 0, 0, 0, 0, 4F, 0.65,0.45,0.95, 1, 1, 18, true, false, new ParticleComponent[]{
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, new ParticleComponent.Expression(t -> (float) Math.pow(t, 0.6) * 14f), false),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, new ParticleComponent.Expression(t -> 1f - (float) Math.pow(t, 0.5)), false),
            });
        }
    }

    @Override
    public void minecartParticles(Level world, AbstractMinecart minecart, float scale, double x, double y, double z, BlockState state, BlockPos pos) {
        if (!(world instanceof ClientLevel clientLevel)) {
            return;
        }

        final int size = 3;
        float offset =  -0.5F * scale;
        for (int ix = 0; ix < size; ix++) {
            for (int iy = 0; iy < size; iy++) {
                for (int iz = 0; iz < size; iz++) {
                    double dx = (double) ix / size * scale;
                    double dy = (double) iy / size * scale;
                    double dz = (double) iz / size * scale;
                    Vec3 minecartMotion = minecart.getDeltaMovement();
                    TerrainParticle terrainParticle = new TerrainParticle(
                            clientLevel,
                            x + dx + offset, y + dy + offset, z + dz + offset,
                            dx + minecartMotion.x(), dy + minecartMotion.y(), dz + minecartMotion.z(),
                            state
                    ) {};
                    Minecraft.getInstance().particleEngine.add(((TerrainParticleExtensions) (Object) terrainParticle).updateSprite(state, pos));
                }
            }
        }
    }

    public void setTPS(float tickRate) {

    }

    public Entity getReferencedMob() {
        return referencedMob;
    }

    public void setReferencedMob(Entity referencedMob) {
        this.referencedMob = referencedMob;
    }

    public void sculptorMarkBlock(int id, BlockPos pos) {
        SculptorBlockMarking blockMarking = sculptorMarkedBlocks.get(pos.asLong());
        if (blockMarking == null) {
            blockMarking = new SculptorBlockMarking(pos);
            sculptorMarkedBlocks.put(pos.asLong(), blockMarking);
        }
        else {
            blockMarking.resetTick();
        }
    }

    public void updateMarkedBlocks() {
        Iterator<SculptorBlockMarking> iterator = sculptorMarkedBlocks.values().iterator();

        while(iterator.hasNext()) {
            SculptorBlockMarking blockMarking = iterator.next();
            int i = blockMarking.getTicks();
            if (i > blockMarking.getDuration()) {
                iterator.remove();
            }
            blockMarking.tick();
        }
    }

    @Override
    public @Nullable Player getLocalPlayer() {
        return Minecraft.getInstance().player;
    }

    @Override
    public @Nullable Level getClientLevel() {
        return Minecraft.getInstance().level;
    }

    @Override
    public void stopMusic() {
        Minecraft.getInstance().getMusicManager().stopPlaying();
    }

    @Override
    public void playMusic(final BossMusicSound music) {
        Minecraft.getInstance().getSoundManager().play(music);
    }
}
