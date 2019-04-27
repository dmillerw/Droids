package me.dmillerw.droids.common.item;

import me.dmillerw.droids.common.ModInfo;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class BaseItem extends Item {

    public BaseItem(String key) {
        setCreativeTab(ModInfo.TAB);
        setRegistryName(new ResourceLocation(ModInfo.ID, key));
        setUnlocalizedName(key);
    }
}
