package me.dmillerw.droids.api;

import me.dmillerw.droids.common.entity.ai.AIHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IDroid {

//    public ItemStack getTool();
//    public NonNullList<ItemStack> getInventory();

    public World getWorld();
    public ItemStack getHeldItem();
    public AIHelper getAiHelper();
}
