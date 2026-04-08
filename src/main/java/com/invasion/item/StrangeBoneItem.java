package com.invasion.item;

import com.invasion.nexus.Mode;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import com.invasion.entity.IMWolfEntity;
import com.invasion.entity.InvEntities;
import com.invasion.nexus.IHasNexus;
import com.invasion.nexus.NexusAccess;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;

import java.util.UUID;

class StrangeBoneItem extends Item {
    public StrangeBoneItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (!(entity instanceof WolfEntity wolf) || entity instanceof IMWolfEntity) {
            return ActionResult.PASS;
        }
        @Nullable
        NexusAccess nexus = IHasNexus.findNexus(entity.getWorld(), entity.getBlockPos());
        if (nexus == null || nexus.getMode() == Mode.STOPPED) {
            user.sendMessage(Text.translatable("invmod.message.bone.nonearbynexus1").formatted(Formatting.RED), true);
            return ActionResult.FAIL;
        }

        if (!wolf.isTamed()) {
            wolf.setOwner(user);
            wolf.setOwnerUuid(user.getUuid());
        }

        IMWolfEntity newWolf = wolf.convertTo(InvEntities.WOLF,true);
//        newWolf.copyFrom(wolf);
//        newWolf.setUuid(UUID.randomUUID());
//        newWolf.setNexus(nexus);

//        World wolfWorld = wolf.getWorld();
//        wolf.discard();
//        wolfWorld.spawnEntity(newWolf);

        stack.decrement(1);
        return ActionResult.SUCCESS;
    }
}