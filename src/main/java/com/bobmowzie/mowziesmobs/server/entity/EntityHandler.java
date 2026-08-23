package com.bobmowzie.mowziesmobs.server.entity;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.server.entity.bluff.EntityBluff;
import com.bobmowzie.mowziesmobs.server.entity.effects.*;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.*;
import com.bobmowzie.mowziesmobs.server.entity.elokosa.EntityElokosa;
import com.bobmowzie.mowziesmobs.server.entity.elokosa.EntityElokosaFollowerToHowler;
import com.bobmowzie.mowziesmobs.server.entity.elokosa.EntityElokosaHowler;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityBabyFoliaath;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityFoliaath;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrozenController;
import com.bobmowzie.mowziesmobs.server.entity.grottol.EntityGrottol;
import com.bobmowzie.mowziesmobs.server.entity.lantern.EntityLantern;
import com.bobmowzie.mowziesmobs.server.entity.naga.EntityNaga;
import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.*;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.trade.Trade;
import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import io.github.fabricators_of_create.porting_lib.registry.DeferredHolder;
import io.github.fabricators_of_create.porting_lib.registry.DeferredRegister;
import io.github.fabricators_of_create.porting_lib.entity.data.PortingLibEntityDataSerializers;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.world.entity.EntityDimensions;

import java.util.Optional;

public class EntityHandler {
    public static final DeferredRegister<EntityDataSerializer<?>> SERIALIZER_REG = DeferredRegister.create(PortingLibEntityDataSerializers.ENTITY_DATA_SERIALIZERS_KEY, MMCommon.MODID);
    public static DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<Optional<Trade>>> OPTIONAL_TRADE = SERIALIZER_REG.register("optional_trade", () -> EntityDataSerializer.forValueType(Trade.STREAM_CODEC.apply(ByteBufCodecs::optional)));

    public static final DeferredRegister<EntityType<?>> REG = DeferredRegister.create(Registries.ENTITY_TYPE, MMCommon.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<EntityFoliaath>> FOLIAATH = REG.register("foliaath", () -> FabricEntityTypeBuilder.create(MobCategory.MONSTER, EntityFoliaath::new).dimensions(EntityDimensions.scalable(0.5f, 2.5f)).trackRangeChunks(8).build());
    public static final DeferredHolder<EntityType<?>, EntityType<EntityBabyFoliaath>> BABY_FOLIAATH = REG.register("baby_foliaath", () -> FabricEntityTypeBuilder.create(MobCategory.MONSTER, EntityBabyFoliaath::new).trackRangeChunks(8).dimensions(EntityDimensions.scalable(0.4f, 0.4f)).trackRangeChunks(8).build());
    public static final DeferredHolder<EntityType<?>, EntityType<EntityWroughtnaut>> WROUGHTNAUT = REG.register("ferrous_wroughtnaut", () -> FabricEntityTypeBuilder.create(MobCategory.MONSTER, EntityWroughtnaut::new).trackRangeChunks(8).dimensions(EntityDimensions.scalable(2.5f, 3.5f)).trackRangeChunks(8).trackedUpdateRate(1).build());
    private static FabricEntityTypeBuilder<EntityUmvuthanaFollowerToRaptor> umvuthanaFollowerToRaptorBuilder() {
        return FabricEntityTypeBuilder.create(MobCategory.MONSTER, EntityUmvuthanaFollowerToRaptor::new);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityUmvuthanaFollowerToRaptor>> UMVUTHANA_FOLLOWER_TO_RAPTOR = REG.register("umvuthana_follower_raptor", () -> umvuthanaFollowerToRaptorBuilder().dimensions(EntityDimensions.scalable(MaskType.FEAR.entityWidth, MaskType.FEAR.entityHeight)).trackRangeChunks(8).trackedUpdateRate(1).build());
    private static FabricEntityTypeBuilder<EntityUmvuthanaFollowerToPlayer> umvuthanaFollowerToPlayerBuilder() {
        return FabricEntityTypeBuilder.create(MobCategory.MONSTER, EntityUmvuthanaFollowerToPlayer::new);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityUmvuthanaFollowerToPlayer>> UMVUTHANA_FOLLOWER_TO_PLAYER = REG.register("umvuthana_follower_player", () -> umvuthanaFollowerToPlayerBuilder().dimensions(EntityDimensions.scalable(MaskType.FEAR.entityWidth, MaskType.FEAR.entityHeight)).trackRangeChunks(8).trackedUpdateRate(1).build());
    private static FabricEntityTypeBuilder<EntityUmvuthanaCraneToPlayer> umvuthanaCraneToPlayerBuilder() {
        return FabricEntityTypeBuilder.create(MobCategory.MONSTER, EntityUmvuthanaCraneToPlayer::new);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityUmvuthanaCraneToPlayer>> UMVUTHANA_CRANE_TO_PLAYER = REG.register("umvuthana_crane_player", () -> umvuthanaCraneToPlayerBuilder().dimensions(EntityDimensions.scalable(MaskType.FAITH.entityWidth, MaskType.FAITH.entityHeight)).trackRangeChunks(8).trackedUpdateRate(1).build());
    public static final DeferredHolder<EntityType<?>, EntityType<EntityUmvuthanaMinion>> UMVUTHANA_MINION = REG.register("umvuthana", () -> FabricEntityTypeBuilder.create(MobCategory.MONSTER, EntityUmvuthanaMinion::new).dimensions(EntityDimensions.scalable(MaskType.FEAR.entityWidth, MaskType.FEAR.entityHeight)).trackRangeChunks(8).trackedUpdateRate(1).build());
    public static final DeferredHolder<EntityType<?>, EntityType<EntityUmvuthanaRaptor>> UMVUTHANA_RAPTOR = REG.register("umvuthana_raptor", () -> FabricEntityTypeBuilder.create(MobCategory.MONSTER, EntityUmvuthanaRaptor::new).dimensions(EntityDimensions.scalable(MaskType.FURY.entityWidth, MaskType.FURY.entityHeight)).trackRangeChunks(8).trackedUpdateRate(1).build());
    public static final DeferredHolder<EntityType<?>, EntityType<EntityUmvuthanaCrane>> UMVUTHANA_CRANE = REG.register("umvuthana_crane", () -> FabricEntityTypeBuilder.create(MobCategory.MONSTER, EntityUmvuthanaCrane::new).dimensions(EntityDimensions.scalable(MaskType.FEAR.entityWidth, MaskType.FEAR.entityHeight)).trackRangeChunks(8).trackedUpdateRate(1).build());
    public static final DeferredHolder<EntityType<?>, EntityType<EntityUmvuthi>> UMVUTHI = REG.register("umvuthi", () -> FabricEntityTypeBuilder.create(MobCategory.MONSTER, EntityUmvuthi::new).dimensions(EntityDimensions.scalable(1.5f, 3.2f)).trackRangeChunks(10).trackedUpdateRate(1).build());
    public static final DeferredHolder<EntityType<?>, EntityType<EntityFrostmaw>> FROSTMAW = REG.register("frostmaw", () -> FabricEntityTypeBuilder.create(MobCategory.MONSTER, EntityFrostmaw::new).dimensions(EntityDimensions.scalable(4f, 4f)).trackRangeChunks(10).trackedUpdateRate(1).build());
    public static final DeferredHolder<EntityType<?>, EntityType<EntityGrottol>> GROTTOL = REG.register("grottol", () -> FabricEntityTypeBuilder.create(MobCategory.MONSTER, EntityGrottol::new).dimensions(EntityDimensions.scalable(0.9F, 1.2F)).trackRangeChunks(8).trackedUpdateRate(1).build());
    public static final DeferredHolder<EntityType<?>, EntityType<EntityLantern>> LANTERN = REG.register("lantern", () -> FabricEntityTypeBuilder.create(MobCategory.AMBIENT, EntityLantern::new).dimensions(EntityDimensions.scalable(1.0f, 1.0f)).trackRangeChunks(8).trackedUpdateRate(1).build());
    public static final DeferredHolder<EntityType<?>, EntityType<EntityNaga>> NAGA = REG.register("naga", () -> FabricEntityTypeBuilder.create(MobCategory.MONSTER, EntityNaga::new).dimensions(EntityDimensions.scalable(3.0f, 1.0f)).trackRangeChunks(13).spawnableFarFromPlayer().trackedUpdateRate(1).build());
    public static final DeferredHolder<EntityType<?>, EntityType<EntitySculptor>> SCULPTOR = REG.register("sculptor", () -> FabricEntityTypeBuilder.create(MobCategory.MISC, EntitySculptor::new).dimensions(EntityDimensions.scalable(1.0f, 2.3f)).trackRangeChunks(8).trackedUpdateRate(1).build());
    public static final DeferredHolder<EntityType<?>, EntityType<EntityBluff>> BLUFF = REG.register("bluff", () -> FabricEntityTypeBuilder.create(MobCategory.MONSTER, EntityBluff::new).dimensions(EntityDimensions.scalable(0.6F, 1.8F)).trackRangeChunks(8).trackedUpdateRate(1).build());
    private static FabricEntityTypeBuilder<EntityElokosaFollowerToHowler> elokosaFollowerToHowlerBuilder() {
        return FabricEntityTypeBuilder.create(MobCategory.MONSTER, EntityElokosaFollowerToHowler::new);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityElokosaFollowerToHowler>> ELOKOSA_FOLLOWER_TO_HOWLER = REG.register("elokosa_follower_howler", () -> elokosaFollowerToHowlerBuilder().dimensions(EntityDimensions.scalable(0.9F, 1.6F)).trackRangeChunks(8).trackedUpdateRate(1).build());
    public static final DeferredHolder<EntityType<?>, EntityType<EntityElokosaHowler>> ELOKOSA_HOWLER = REG.register("elokosa_howler", () -> FabricEntityTypeBuilder.create(MobCategory.MONSTER, EntityElokosaHowler::new).dimensions(EntityDimensions.scalable(0.95F, 1.7F)).trackRangeChunks(8).trackedUpdateRate(1).build());

    private static FabricEntityTypeBuilder<EntitySunstrike> sunstrikeBuilder() {
        return FabricEntityTypeBuilder.create(MobCategory.MISC, EntitySunstrike::new);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntitySunstrike>> SUNSTRIKE = REG.register("sunstrike", () -> sunstrikeBuilder().dimensions(EntityDimensions.scalable(0.1F, 0.1F)).build());
    private static FabricEntityTypeBuilder<EntitySolarBeam> solarBeamBuilder() {
        return FabricEntityTypeBuilder.create(MobCategory.MISC, EntitySolarBeam::new);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntitySolarBeam>> SOLAR_BEAM = REG.register("solar_beam", () -> solarBeamBuilder().dimensions(EntityDimensions.scalable(0.1F, 0.1F)).trackedUpdateRate(1).build());
    private static FabricEntityTypeBuilder<EntityBoulderProjectile> boulderProjectileBuilder() {
        return FabricEntityTypeBuilder.create(MobCategory.MISC, EntityBoulderProjectile::new);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityBoulderProjectile>> BOULDER_PROJECTILE = REG.register("boulder_projectile", () -> boulderProjectileBuilder().dimensions(EntityDimensions.scalable(1, 1)).trackedUpdateRate(1).build());
    private static FabricEntityTypeBuilder<EntityRockSling> rockSlingBuilder() {
        return FabricEntityTypeBuilder.create(MobCategory.MISC, EntityRockSling::new);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityRockSling>> ROCK_SLING = REG.register("rock_sling", () -> rockSlingBuilder().dimensions(EntityDimensions.scalable(0.5f, 0.5f)).trackedUpdateRate(1).build());
    private static FabricEntityTypeBuilder<EntityBoulderSculptor> boulderPlatformBuilder() {
        return FabricEntityTypeBuilder.create(MobCategory.MISC, EntityBoulderSculptor::new);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityBoulderSculptor>> BOULDER_SCULPTOR = REG.register("boulder_platform", () -> boulderPlatformBuilder().dimensions(EntityDimensions.scalable(1, 1)).trackedUpdateRate(1).build());
    private static FabricEntityTypeBuilder<EntityBoulderSculptor.EntityBoulderSculptorCrumbling> boulderPlatformCrumblingBuilder() {
        return FabricEntityTypeBuilder.create(MobCategory.MISC, EntityBoulderSculptor.EntityBoulderSculptorCrumbling::new);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityBoulderSculptor.EntityBoulderSculptorCrumbling>> BOULDER_SCULPTOR_CRUMBLING = REG.register("boulder_platform_crumbling", () -> boulderPlatformCrumblingBuilder().dimensions(EntityDimensions.scalable(1, 1)).trackedUpdateRate(1).build());
    private static FabricEntityTypeBuilder<EntityPillar> pillarBuilder() {
        return FabricEntityTypeBuilder.create(MobCategory.MISC, EntityPillar::new);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityPillar>> PILLAR = REG.register("pillar", () -> pillarBuilder().dimensions(EntityDimensions.scalable(1f, 1f)).trackedUpdateRate(1).build());
    private static FabricEntityTypeBuilder<EntityPillar.EntityPillarSculptor> sculptorPillarBuilder() {
        return FabricEntityTypeBuilder.create(MobCategory.MISC, EntityPillar.EntityPillarSculptor::new);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityPillar.EntityPillarSculptor>> PILLAR_SCULPTOR = REG.register("pillar_sculptor", () -> sculptorPillarBuilder().dimensions(EntityDimensions.scalable(1f, 1f)).trackRangeChunks(8).trackedUpdateRate(1).build());
    private static FabricEntityTypeBuilder<EntityPillarPiece> pillarPieceBuilder() {
        return FabricEntityTypeBuilder.create(MobCategory.MISC, EntityPillarPiece::new);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityPillarPiece>> PILLAR_PIECE = REG.register("pillar_piece", () -> pillarPieceBuilder().dimensions(EntityDimensions.scalable(1f, 1f)).trackedUpdateRate(1).build());

    private static FabricEntityTypeBuilder<EntityAxeAttack> axeAttackBuilder() {
        return FabricEntityTypeBuilder.create(MobCategory.MISC, EntityAxeAttack::new);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityAxeAttack>> AXE_ATTACK = REG.register("axe_attack", () -> axeAttackBuilder().dimensions(EntityDimensions.scalable(1f, 1f)).trackedUpdateRate(1).build());
    private static FabricEntityTypeBuilder<EntityIceBreath> iceBreathBuilder() {
        return FabricEntityTypeBuilder.create(MobCategory.MISC, EntityIceBreath::new);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityIceBreath>> ICE_BREATH = REG.register("ice_breath", () -> iceBreathBuilder().dimensions(EntityDimensions.scalable(0F, 0F)).trackedUpdateRate(1).build());
    private static FabricEntityTypeBuilder<EntityIceBall> iceBallBuilder() {
        return FabricEntityTypeBuilder.create(MobCategory.MISC, EntityIceBall::new);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityIceBall>> ICE_BALL = REG.register("ice_ball", () -> iceBallBuilder().dimensions(EntityDimensions.scalable(0.5F, 0.5F)).trackedUpdateRate(20).build());
    private static FabricEntityTypeBuilder<EntityFrozenController> frozenControllerBuilder() {
        return FabricEntityTypeBuilder.create(MobCategory.MISC, EntityFrozenController::new);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityFrozenController>> FROZEN_CONTROLLER = REG.register("frozen_controller", () -> frozenControllerBuilder().disableSummon().dimensions(EntityDimensions.scalable(0, 0)).build());
    private static FabricEntityTypeBuilder<EntityDart> dartBuilder() {
        return FabricEntityTypeBuilder.create(MobCategory.MISC, EntityDart::new);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityDart>> DART = REG.register("dart", () -> dartBuilder().disableSummon().dimensions(EntityDimensions.scalable(0.5F, 0.5F)).trackedUpdateRate(20).build());
    private static FabricEntityTypeBuilder<EntityPoisonBall> poisonBallBuilder() {
        return FabricEntityTypeBuilder.create(MobCategory.MISC, EntityPoisonBall::new);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityPoisonBall>> POISON_BALL = REG.register("poison_ball", () -> poisonBallBuilder().dimensions(EntityDimensions.scalable(0.5F, 0.5F)).trackedUpdateRate(20).build());
    private static FabricEntityTypeBuilder<EntitySuperNova> superNovaBuilder() {
        return FabricEntityTypeBuilder.create(MobCategory.MISC, EntitySuperNova::new);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntitySuperNova>> SUPER_NOVA = REG.register("super_nova", () -> superNovaBuilder().dimensions(EntityDimensions.scalable(1, 1)).trackedUpdateRate(Integer.MAX_VALUE).build());
    private static FabricEntityTypeBuilder<EntityFallingBlock> fallingBlockBuilder() {
        return FabricEntityTypeBuilder.create(MobCategory.MISC, EntityFallingBlock::new);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityFallingBlock>> FALLING_BLOCK = REG.register("falling_block", () -> fallingBlockBuilder().dimensions(EntityDimensions.scalable(1, 1)).build());
    private static FabricEntityTypeBuilder<EntityBlockSwapper> blockSwapperBuilder() {
        return FabricEntityTypeBuilder.create(MobCategory.MISC, EntityBlockSwapper::new);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityBlockSwapper>> BLOCK_SWAPPER = REG.register("block_swapper", () -> blockSwapperBuilder().disableSummon().dimensions(EntityDimensions.scalable(1, 1)).trackedUpdateRate(Integer.MAX_VALUE).build());
    private static FabricEntityTypeBuilder<EntityBlockSwapper.EntityBlockSwapperTunneling> blockSwapperTunnelingBuilder() {
        return FabricEntityTypeBuilder.create(MobCategory.MISC, EntityBlockSwapper.EntityBlockSwapperTunneling::new);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityBlockSwapper.EntityBlockSwapperTunneling>> BLOCK_SWAPPER_TUNNELING = REG.register("block_swapper_tunneling", () -> blockSwapperTunnelingBuilder().disableSummon().dimensions(EntityDimensions.scalable(1, 1)).trackedUpdateRate(Integer.MAX_VALUE).build());
    private static FabricEntityTypeBuilder<EntityCameraShake> cameraShakeBuilder() {
        return FabricEntityTypeBuilder.create(MobCategory.MISC, EntityCameraShake::new);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityCameraShake>> CAMERA_SHAKE = REG.register("camera_shake", () -> cameraShakeBuilder().dimensions(EntityDimensions.scalable(1, 1)).trackedUpdateRate(Integer.MAX_VALUE).build());
    private static FabricEntityTypeBuilder<EntityFissure> fissureBuilder() {
        return FabricEntityTypeBuilder.create(MobCategory.MISC, EntityFissure::new);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityFissure>> FISSURE = REG.register("fissure", () -> fissureBuilder().dimensions(EntityDimensions.scalable(1f, 1f)).trackedUpdateRate(1).build());
    private static FabricEntityTypeBuilder<EntityFissurePiece> fissurePieceBuilder() {
        return FabricEntityTypeBuilder.create(MobCategory.MISC, EntityFissurePiece::new);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityFissurePiece>> FISSURE_PIECE = REG.register("fissure_piece", () -> fissurePieceBuilder().dimensions(EntityDimensions.scalable(EntityFissurePiece.PIECE_SIZE, 0.1f)).build());
    private static FabricEntityTypeBuilder<EntityEarthSpike> earthSpikeBuilder() {
        return FabricEntityTypeBuilder.create(MobCategory.MISC, EntityEarthSpike::new);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<EntityEarthSpike>> EARTH_SPIKE = REG.register("earth_spike", () -> earthSpikeBuilder().dimensions(EntityDimensions.scalable(1f, 1f)).build());

    public static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(EntityHandler.FOLIAATH.get(), EntityFoliaath.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityHandler.BABY_FOLIAATH.get(), EntityBabyFoliaath.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityHandler.WROUGHTNAUT.get(), EntityWroughtnaut.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityHandler.UMVUTHANA_RAPTOR.get(), EntityUmvuthanaRaptor.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityHandler.UMVUTHANA_MINION.get(), EntityUmvuthana.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityHandler.UMVUTHANA_FOLLOWER_TO_PLAYER.get(), EntityUmvuthanaFollowerToPlayer.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityHandler.UMVUTHANA_CRANE_TO_PLAYER.get(), EntityUmvuthanaFollowerToPlayer.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityHandler.UMVUTHANA_FOLLOWER_TO_RAPTOR.get(), EntityUmvuthana.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityHandler.UMVUTHANA_CRANE.get(), EntityUmvuthana.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityHandler.UMVUTHI.get(), EntityUmvuthi.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityHandler.FROSTMAW.get(), EntityFrostmaw.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityHandler.NAGA.get(), EntityNaga.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityHandler.LANTERN.get(), EntityLantern.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityHandler.GROTTOL.get(), EntityGrottol.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityHandler.SCULPTOR.get(), EntitySculptor.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityHandler.BLUFF.get(), EntityBluff.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityHandler.ELOKOSA_FOLLOWER_TO_HOWLER.get(), EntityElokosa.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityHandler.ELOKOSA_HOWLER.get(), EntityElokosaHowler.createAttributes());
    }
}
