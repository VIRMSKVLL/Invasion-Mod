package com.invasion.entity;

import com.google.common.base.Predicates;
import com.invasion.entity.ai.goal.AttackNexusGoal;
import com.invasion.entity.ai.goal.GoToNexusGoal;
import com.invasion.entity.ai.goal.KillEntityGoal;
import com.invasion.entity.ai.goal.NoNexusPathGoal;
import com.invasion.entity.ai.goal.target.CustomRangeActiveTargetGoal;
import com.invasion.entity.ai.goal.target.RetaliateGoal;
import com.invasion.entity.ai.goal.ProvideSupportGoal;
import com.invasion.item.InvItems;
import com.invasion.particle.InvParticles;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class ImpEntity extends IMMobEntity {
    public ImpEntity(EntityType<ImpEntity> type, World world) {
        super(type, world);
        this.setPathfindingPenalty(PathNodeType.WATER, 100.0F);
        this.setPathfindingPenalty(PathNodeType.LAVA, 0.0F);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, 0.0F);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, 0.0F);
        getNavigatorNew().getActor().setCanClimb(true);
    }

    @Override
    protected void initEquipment(Random random, LocalDifficulty localDifficulty) {
        setStackInHand(Hand.MAIN_HAND, Items.TRIDENT.getDefaultStack());
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3)
                .add(EntityAttributes.GENERIC_STEP_HEIGHT, 1)
                .add(EntityAttributes.GENERIC_STEP_HEIGHT,1)
                ;
    }

    @Override
    public void tick() {
        if (!this.getWorld().isClient()) {
            double distance = 0.5;
            float yaw = this.getBodyYaw();
            float yawRadians = (float) Math.toRadians(yaw);
            double offsetX = -MathHelper.sin(yawRadians) * distance;
            double offsetZ = MathHelper.cos(yawRadians) * distance;
            ServerWorld serverWorld = (ServerWorld) this.getWorld();
            serverWorld.spawnParticles(InvParticles.IMPTAIL,this.getX()-offsetX,this.getY()+0.25,this.getZ()-offsetZ,1,0,0,0,0);
        }
        super.tick();
    }


    @Override
    protected void initGoals() {
        goalSelector.add(0, new SwimGoal(this));
        goalSelector.add(1, new KillEntityGoal<>(this, PlayerEntity.class, 40));
        goalSelector.add(2, new AttackNexusGoal<>(this));
        goalSelector.add(3, new ProvideSupportGoal(this, 4, true));
        goalSelector.add(4, new KillEntityGoal<>(this, MobEntity.class, 40));
        goalSelector.add(5, new GoToNexusGoal(this));
        goalSelector.add(6, new WanderAroundFarGoal(this, 1));
        goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 8));
        goalSelector.add(8, new LookAtEntityGoal(this, IMCreeperEntity.class, 12));
        goalSelector.add(8, new LookAroundGoal(this));

        targetSelector.add(0, new RetaliateGoal(this));
        targetSelector.add(1, new CustomRangeActiveTargetGoal<>(this, PlayerEntity.class, this::getSenseRange, false));
        targetSelector.add(2, new CustomRangeActiveTargetGoal<>(this, PlayerEntity.class, this::getAggroRange, true));
        targetSelector.add(5, new RevengeGoal(this));
        targetSelector.add(3, new NoNexusPathGoal(this, new CustomRangeActiveTargetGoal<>(this, PigmanEngineerEntity.class, 3.5F)));
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.getSource() instanceof SnowballEntity) {
            return super.damage(source, 2.5f);
        }
        return super.damage(source, amount);
    }
    @Override
    public boolean hurtByWater() {
        return true;
    }
    @Override
    public boolean isFireImmune() { return true; }
    @Override
    protected SoundEvent getAmbientSound() { return SoundEvents.ENTITY_VEX_CHARGE; }
    @Override
    public String getLegacyName() { return "IMImp-T1"; };
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_VEX_HURT;
    }
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_VEX_DEATH;
    }
    @Override
    public boolean tryAttack(Entity entity) {
        if (super.tryAttack(entity)) {
            entity.setFireTicks(3);
            return true;
        }
        return false;
    }
}