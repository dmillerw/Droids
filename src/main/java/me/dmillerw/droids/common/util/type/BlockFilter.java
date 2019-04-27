package me.dmillerw.droids.common.util.type;

import net.minecraft.nbt.NBTTagCompound;

public class BlockFilter {

    public static BlockFilter fromNbt(NBTTagCompound tag) {
        return null;
    }

    public static final BlockFilter ALLOW_ALL = new BlockFilter();

    public void readFromNbt(NBTTagCompound tag) {

    }

    public void writeToNbt(NBTTagCompound tag) {

    }
}
