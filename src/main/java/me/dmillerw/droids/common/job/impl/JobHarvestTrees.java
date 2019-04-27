package me.dmillerw.droids.common.job.impl;

import me.dmillerw.droids.api.IAIController;
import me.dmillerw.droids.common.job.JobDefinition;
import me.dmillerw.droids.common.job.JobInstance;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class JobHarvestTrees extends JobDefinition {

    public static final String KEY = "harvest_trees";

    public JobHarvestTrees() {
        super(KEY, "Harvests all trees from a defined area");
    }

    @Override
    public JobInstance createInstance() {
        return new JobHarvestTrees.Instance();
    }

    @Override
    protected ItemStack getRenderIcon() {
        return new ItemStack(Blocks.SAPLING);
    }

    public static class Instance extends JobInstance {

        @Override
        public void tick(IAIController controller) {

        }
    }
}
