package me.dmillerw.droids.common.inventory;

import me.dmillerw.droids.common.item.ItemJobCard;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotJobCard extends Slot {

    public SlotJobCard(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() instanceof ItemJobCard;
    }
}
