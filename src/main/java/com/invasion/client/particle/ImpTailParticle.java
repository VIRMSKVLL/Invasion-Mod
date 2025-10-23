package com.invasion.client.particle;

import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;

public class ImpTailParticle extends SpriteBillboardParticle {

    public static ParticleFactory<SimpleParticleType> factory(SpriteProvider spriteProvider) {
        return (type, world, x, y, z, dX, dY, dZ) -> new ImpTailParticle(world, x, y, z, spriteProvider);
    }

    ImpTailParticle(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider) {
        super(world, x, y, z);
        this.velocityX = 0;
        this.velocityY = 0;
        this.velocityZ = 0;
        gravityStrength = 0;
        maxAge = 2;
        setSpriteForAge(spriteProvider);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_LIT;
    }

    @Override
    public Rotator getRotator() {
        return Rotator.ALL_AXIS;
    }

    @Override
    public void buildGeometry(VertexConsumer buffer, Camera camera, float tickDelta) {
        super.buildGeometry(buffer, camera, tickDelta);
    }

}
