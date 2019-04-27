package me.dmillerw.droids.common.item;

import me.dmillerw.droids.common.ModInfo;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class ItemUpgradeCard extends BaseItem {

    public static final String KEY = "upgrade_card";

    public static enum UpgradeType {

        RANGE(8);

        public final int baseValue;

        private UpgradeType(int baseValue) {
            this.baseValue = baseValue;
        }
    }

    public static enum Upgrade {

        RANGE_1(UpgradeType.RANGE, 1, 0);

        public final UpgradeType type;
        public final int modifier;
        public final int ordinal;

        private Upgrade(UpgradeType type, int modifier, int ordinal) {
            this.type = type;
            this.modifier = modifier;
            this.ordinal = ordinal;
        }
    }

    public static Upgrade getUpgrade(ItemStack stack) {
        if (stack.isEmpty())
            return null;

        if (!(stack.getItem() instanceof ItemUpgradeCard))
            return null;

        for (Upgrade upgrade : Upgrade.values()) {
            if (upgrade.ordinal == stack.getItemDamage()) {
                return upgrade;
            }
        }

        return null;
    }

    public ItemUpgradeCard() {
        super(KEY);

        setHasSubtypes(true);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            for (Upgrade upgrade : Upgrade.values()) {
                items.add(new ItemStack(this, 1, upgrade.ordinal));
            }
        }
    }
}
