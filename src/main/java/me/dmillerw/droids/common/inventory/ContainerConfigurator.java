package me.dmillerw.droids.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerConfigurator extends Container {

    private final InventoryConfigurator inventory;

    public ContainerConfigurator(IInventory playerInventory, ItemStack itemStack) {
        this.inventory = new InventoryConfigurator(itemStack);

        this.addSlotToContainer(new SlotJobCard(inventory, 0, 9, 8));

        for (int i = 0; i < 3; ++i) {
            for (int i1 = 0; i1 < 9; ++i1) {
                this.addSlotToContainer(new Slot(playerInventory, i1 + i * 9 + 9, 8 + i1 * 18, 174 + i * 18));
            }
        }

        for (int j = 0; j < 9; ++j) {
            this.addSlotToContainer(new Slot(playerInventory, j, 8 + j * 18, 232));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }
}
