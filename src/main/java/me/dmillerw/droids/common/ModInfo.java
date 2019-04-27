package me.dmillerw.droids.common;

import me.dmillerw.droids.common.item.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class ModInfo {

    public static final String ID = "droids";
    public static final String NAME = "Droids";
    public static final String VERSION = "0.0.1";

    private static final String BASE_PACKAGE = "me.dmillerw.droids";
    public static final String COMMON_PROXY = BASE_PACKAGE + ".proxy.CommonProxy";
    public static final String CLIENT_PROXY = BASE_PACKAGE + ".proxy.ClientProxy";

    public static final CreativeTab TAB = new CreativeTab();

    public static class CreativeTab extends CreativeTabs {

        public CreativeTab() {
            super(ID);
        }


        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(ModItems.configurator);
        }
    }
}
