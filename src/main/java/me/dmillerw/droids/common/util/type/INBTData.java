package me.dmillerw.droids.common.util.type;

import net.minecraft.nbt.NBTTagCompound;

public interface INBTData {

    public void writeToNbt(NBTTagCompound tag);
    public void readFromNBT(NBTTagCompound tag);
}
