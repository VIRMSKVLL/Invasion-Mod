package com.invasion.entity;

import java.util.Comparator;
import java.util.Optional;

import com.invasion.particle.InvParticles;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.GlobalPos;
import org.jetbrains.annotations.Nullable;

import com.invasion.InvasionMod;
import com.invasion.item.InvItems;
import com.invasion.nexus.IHasNexus;
import com.invasion.nexus.NexusAccess;
import com.invasion.nexus.Mode;

import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class IMWolfEntity extends WolfEntity implements IHasNexus {
    private final IHasNexus.Handle nexus = new IHasNexus.Handle(this::getWorld);
    // TODO: make nexus wolf activatable during nexus event with bone but when nexus ends so does the entity swap and all IMWOlfEntities should transform back to wolves
    public IMWolfEntity(EntityType<IMWolfEntity> type, World world) {
        this(type, world, null);
    }

    public IMWolfEntity(EntityType<IMWolfEntity> type, World world, @Nullable NexusAccess nexus) {
        super(type, world);
        setNexus(nexus);
    }

    @Override
    public void tick() {
        super.tick();
        if (!hasNexus() || getNexus().getMode() == Mode.STOPPED) {
            IMWolfEntity wolf = this;
            WolfEntity newWolf = this.convertTo(EntityType.WOLF, true);
            newWolf.copyFrom(wolf);
        }
    }

    @Override
    public void tickMovement() {
//        for (int i = 0; i < 2; ++i) {
//            ServerWorld world = getServer().getWorld(this.getWorld().getRegistryKey());
//            world.spawnParticles(ParticleTypes.ENCHANT,this.getX(),this.getY()+0.25,this.getZ(),1,0,0,0,0);
//        }
        super.tickMovement();
    }

    @Override
    public void tryTeleportToOwner() {
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return WolfEntity.createWolfAttributes().add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK,2.4F).add(EntityAttributes.GENERIC_ATTACK_DAMAGE,6F).add(EntityAttributes.GENERIC_MOVEMENT_SPEED,0.5F);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        targetSelector.add(8, new ActiveTargetGoal<>(this, HostileEntity.class, true));
    }

    @Override
    public Handle getNexusHandle() {
        return nexus;
    }

    @Override
    public double findDistanceToNexus() {
        return nexus.getPos().map(pos -> {
            return Math.sqrt(pos.pos().toCenterPos().squaredDistanceTo(getX(), getBodyY(0.5), getZ()));
        }).orElse(Double.MAX_VALUE);
    }

    @Override
    public boolean tryAttack(Entity target) {
        boolean success = super.tryAttack(target);
            if (success) {
            heal(4);
            getWorld().sendEntityStatus(this, EntityStatuses.ADD_BREEDING_PARTICLES);
        }
        if (Math.random() < 0.50 && target instanceof Stunnable) {
            ((Stunnable) target).stun(20*4);
        }
        return success;
    }


    @Override
    protected void updatePostDeath() {
        if (++deathTime >= 120) {
            getWorld().sendEntityStatus(this, EntityStatuses.ADD_DEATH_PARTICLES);
            for (int j = 0; j < 20; j++) {
                getWorld().addParticle(ParticleTypes.EXPLOSION,
                        getParticleX(2),
                        getRandomBodyY(),
                        getParticleZ(2),
                        getRandom().nextGaussian() * 0.02D,
                        getRandom().nextGaussian() * 0.02D,
                        getRandom().nextGaussian() * 0.02D
                );
            }
            if (!respawnAtNexus()) {
                remove(Entity.RemovalReason.KILLED);
            }
        }
    }

    public boolean respawnAtNexus() {
        if (!hasNexus() || getNexus().getMode() == Mode.STOPPED) {
            return false;
        }
        deathTime = 0;

        return nexus.getPos().filter(center -> {
            IMWolfEntity wolf = InvEntities.WOLF.create(this.getWorld());
            Optional<Vec3d> respawnPoint = BlockPos.streamOutwards(center.pos(), 5, 3, 5).map(BlockPos::toBottomCenterPos)
                    .filter(pos -> {
                        wolf.setPosition(pos);
                        return wolf.canSpawn(getWorld(), SpawnReason.MOB_SUMMONED);
                    }).sorted(Comparator.comparingDouble(pos -> center.pos().getSquaredDistance(pos.x, pos.y, pos.z)))
                    .findAny();

            if (respawnPoint.isPresent()) {
                wolf.copyFrom(this);
                wolf.setHealth(40.0F);
                wolf.setNexus(getNexus());
                wolf.setPosition(respawnPoint.get().add(new Vec3d(0.0F,1.0F,0.0F)));
                if (!isRemoved()) { discard(); }
                getWorld().spawnEntity(wolf);
                return true;
            }
            InvasionMod.LOGGER.warn("No respawn spot for wolf");
            return false;
        }).isPresent();
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        nexus.writeNbt(compound);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        nexus.readNbt(compound);
    }
}