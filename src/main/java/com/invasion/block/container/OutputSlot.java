package com.invasion.block.container;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class OutputSlot extends Slot {
    public OutputSlot(Inventory iinventory, int i, int j, int k) {
        super(iinventory, i, j, k);
    }
    @Override
    public boolean canInsert(ItemStack stack) {
        return false;
    }
}