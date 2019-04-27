package me.dmillerw.droids.common.job.impl;

import me.dmillerw.droids.api.IAIController;
import me.dmillerw.droids.common.job.JobDefinition;
import me.dmillerw.droids.common.job.JobInstance;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class JobCollectItems extends JobDefinition {

    public static final String KEY = "collect_items";

    public JobCollectItems() {
        super(KEY, "Collects all items found on the ground from a defined area");
    }

    @Override
    public JobInstance createInstance() {
        return new JobCollectItems.Instance();
    }

    @Override
    protected ItemStack getRenderIcon() {
        return new ItemStack(Blocks.HOPPER);
    }

    public static class Instance extends JobInstance {

        @Override
        public void tick(IAIController controller) {

        }
    }
}
