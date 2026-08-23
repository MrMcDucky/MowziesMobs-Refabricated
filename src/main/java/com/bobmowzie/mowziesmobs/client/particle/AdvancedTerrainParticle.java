package com.bobmowzie.mowziesmobs.client.particle;

import com.bobmowzie.mowziesmobs.client.particle.types.AdvancedParticleType;
import com.bobmowzie.mowziesmobs.client.particle.types.TerrainParticleType;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleRotation;
import com.bobmowzie.mowziesmobs.client.render.MMRenderType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import io.github.fabricators_of_create.porting_lib.models.CustomParticleIconModel;
import net.fabricmc.fabric.api.renderer.v1.model.WrapperBakedModel;
import net.fabricmc.fabric.api.blockview.v2.FabricBlockView;
import net.minecraft.client.resources.model.BakedModel;
import org.jetbrains.annotations.NotNull;

// FIXME: Terrain particles don't render
public class AdvancedTerrainParticle extends AdvancedParticleBase {
    private final BlockPos pos;
    private final float uo;
    private final float vo;

    protected AdvancedTerrainParticle(ClientLevel worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double motionX, double motionY, double motionZ, double scale, double drag, double duration, boolean canCollide, BlockState state, BlockPos pos, ParticleComponent[] components) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, motionX, motionY, motionZ, new ParticleRotation.FaceCamera(0), scale, 1.0, 1.0, 1.0, 1.0, drag, duration, false, canCollide, components);
        this.pos = pos;
        this.setSprite(Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getParticleIcon(state));
        this.red = 0.6F;
        this.green = 0.6F;
        this.blue = 0.6F;

        int i = Minecraft.getInstance().getBlockColors().getColor(state, worldIn, pos, 0);
        this.red *= (float)(i >> 16 & 255) / 255.0F;
        this.green *= (float)(i >> 8 & 255) / 255.0F;
        this.blue *= (float)(i & 255) / 255.0F;

        this.quadSize /= 2.0F;
        this.uo = this.random.nextFloat() * 3.0F;
        this.vo = this.random.nextFloat() * 3.0F;
    }

    public Particle updateSprite(BlockState state, BlockPos pos) { //FORGE: we cannot assume that the x y z of the particles match the block pos of the block.
        if (pos != null) { // Preserve model-data-aware particle icons when the model provides one.
            Minecraft mc = Minecraft.getInstance();
            BakedModel model = mc.getModelManager().getBlockModelShaper().getBlockModel(state);
            while (model instanceof WrapperBakedModel wrapped) {
                if (model instanceof CustomParticleIconModel) break;
                model = wrapped.getWrappedModel();
                if (model == null) break;
            }
            if (model instanceof CustomParticleIconModel custom && mc.level != null) {
                Object data = ((FabricBlockView) mc.level).getBlockEntityRenderData(pos);
                this.setSprite(custom.getParticleIcon(data));
            } else if (model != null) {
                this.setSprite(model.getParticleIcon());
            }
        }
        return this;
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return MMRenderType.TERRAIN_SHEET_NO_CULL;
    }

    @Override
    protected float getU0() {
        return this.sprite.getU((this.uo + 1.0F) / 4.0F);
    }

    @Override
    protected float getU1() {
        return this.sprite.getU(this.uo / 4.0F);
    }

    @Override
    protected float getV0() {
        return this.sprite.getV(this.vo / 4.0F);
    }

    @Override
    protected float getV1() {
        return this.sprite.getV((this.vo + 1.0F) / 4.0F);
    }

    @Override
    public int getLightColor(float p_108291_) {
        int i = super.getLightColor(p_108291_);
        return i == 0 && this.level.hasChunkAt(this.pos) ? LevelRenderer.getLightColor(this.level, this.pos) : i;
    }

    public static class Factory implements ParticleProvider<TerrainParticleType> {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle createParticle(TerrainParticleType typeIn, @NotNull ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            BlockState blockstate = typeIn.state();

            if (blockstate.isAir() || blockstate.is(Blocks.MOVING_PISTON)) {
                return null;
            }

            AdvancedTerrainParticle particle = new AdvancedTerrainParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.scale(), typeIn.airDrag(), typeIn.duration(), typeIn.canCollide(), typeIn.state(), BlockPos.ZERO, typeIn.components());
            particle.setColor(typeIn.red(), typeIn.green(), typeIn.blue());
            particle.updateSprite(blockstate, typeIn.position());
            return particle;
        }
    }

    public static void spawnTerrainParticle(Level world, Holder<ParticleType<?>> particle, double x, double y, double z, double motionX, double motionY, double motionZ, double rotation, double scale, double drag, double duration, BlockState state, ParticleComponent[] components) {
        AdvancedParticleType base = new AdvancedParticleType(particle, new ParticleRotation.FaceCamera((float) rotation), components, 0.6f, 0.6f, 0.6f, 1, (float) scale, (float) duration, (float) drag, false, false);
        world.addParticle(new TerrainParticleType(base, state), x, y, z, motionX, motionY, motionZ);
    }
}
