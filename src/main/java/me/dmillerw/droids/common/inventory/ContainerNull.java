package me.dmillerw.droids.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerNull extends Container {

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }
}
