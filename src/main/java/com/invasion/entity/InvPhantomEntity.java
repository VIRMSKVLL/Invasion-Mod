package com.invasion.entity;

import com.invasion.InvasionMod;
import com.invasion.nexus.IHasNexus;
import com.invasion.nexus.NexusAccess;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.*;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.List;

public class InvPhantomEntity extends PhantomEntity implements NexusFlyingEntity, Stunnable {
    public InvPhantomEntity(EntityType<? extends PhantomEntity> entityType, World world) {
        super(entityType, world);
    }
    @Override
    protected void initGoals() {
        super.initGoals();
    }
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        this.setPosition(getX(),120,getZ());
        if (getNexus() != null) {
            generateJockey(world, this.getNexus().getCurrentWave(), world.getLocalDifficulty(this.getBlockPos()), SpawnReason.JOCKEY);
        }
        return super.initialize(world,difficulty,spawnReason,entityData);
    }
    @Override
    public String getLegacyName() { return "IMPhantom"; };
    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2)
                ;
    }
    private final IHasNexus.Handle nexus = new IHasNexus.Handle(this::getWorld);
    @Override
    public boolean stun(int maxTicks) {
        return false;
    }
    @Override
    public boolean isStunned() {
        return false;
    }
    @Override
    public Handle getNexusHandle() {
        return nexus;
    }
    @Override
    public FlyingEntity asEntity() {
        return this;
    }
    @Override
    public boolean isAffectedByDaylight() { return false; }
    public void deployJockey() {
        if (this.hasControllingPassenger()) {
            HostileEntity jockey = (HostileEntity) this.getControllingPassenger();
            jockey.dismountVehicle();
            jockey.setAiDisabled(false);
        }
    }
    @Override
    public void tick() {
        if (this.hasControllingPassenger()) {
            int groundDistance = 0;
            for (double y = (this.getY() - 1); y >= -64; y--){
                BlockState state = this.getWorld().getBlockState(new BlockPos((int) getX(), (int) y, (int) getZ()));
                if (!state.isAir()) { break; }
                groundDistance++;
            }
            if (groundDistance < 12 ) {
                deployJockey();
            }
        }
        NexusAccess nexus = this.getNexus();
        if (nexus != null) {
            this.refCirclingCenter(nexus.getOrigin().offset(Direction.UP, 16));
        }
        super.tick();
    }
    @Override
    public void onDeath(DamageSource source) {
        deployJockey();
    }
    // Reflections :((((
    public Vec3d refTargetPosition(Vec3d value) {
        try {
            Field ref = PhantomEntity.class.getDeclaredField("targetPosition");
            ref.setAccessible(true);
            if (value != null) { ref.set(this,value); }
            return (Vec3d) ref.get(this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return new Vec3d(0,0,0);
    }
    public BlockPos refCirclingCenter(BlockPos value) {
        try {
            Field ref = PhantomEntity.class.getDeclaredField("circlingCenter");
            ref.setAccessible(true);
            if (value != null) { ref.set(this,value); }
            return (BlockPos) ref.get(this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return new BlockPos(0,0,0);
    }
    public Object refMovementType(Object value) {
        try {
            Field ref = PhantomEntity.class.getDeclaredField("movementType");
            ref.setAccessible(true);
            if (value != null) { ref.set(this,value); }
            return (Object) ref.get(this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
    // Mountable Port
    void generateJockey(ServerWorldAccess world, int currentWave, LocalDifficulty difficulty, SpawnReason spawnReason) {
        int jockyAttempsts = currentWave * 2;
        while (--jockyAttempsts > 0) {
            Random random = world.getRandom();
            if (random.nextInt(25) == 0) {
                HostileEntity jockey = getJockeyType(world).create(this.getWorld());
                if (jockey != null) {
                    if (jockey instanceof NexusSpiderEntity) {
                        jockey.setBaby(true);
                    }
                    jockey.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), 0.0F);
                    jockey.initialize(world, difficulty, spawnReason, null);
                    // Set Nexus
                    if (jockey instanceof NexusEntity n) {
                        n.setNexus(this.getNexus());
                    }
                    AttributeUtil.applyNexusWaveComplications(jockey, world, currentWave / 2, difficulty, spawnReason);
                    // Lock AI so that the Phantom doesn't freak out
                    jockey.setAiDisabled(true);
                    jockey.startRiding(this);
                    break;
                }
            }
        }
    }
    EntityType<? extends HostileEntity> getJockeyType(ServerWorldAccess world) {
        return Util.getRandom(List.of(
                InvEntities.SKELETON,
                InvEntities.ZOMBIE,
                InvEntities.ZOMBIE_PIGMAN,
                InvEntities.JUMPING_SPIDER,
                InvEntities.SPIDER,
                InvEntities.CREEPER
        ), world.getRandom());
    }

}