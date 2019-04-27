package me.dmillerw.droids.common.util.type;

import net.minecraft.nbt.NBTTagCompound;

public class ItemFilter {

    public static ItemFilter fromNbt(NBTTagCompound tag) {
        return null;
    }

    public static final ItemFilter ALLOW_ALL = new ItemFilter();

    public void readFromNbt(NBTTagCompound tag) {

    }

    public void writeToNbt(NBTTagCompound tag) {

    }
}
