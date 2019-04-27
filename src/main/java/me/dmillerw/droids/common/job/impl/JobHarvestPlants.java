package me.dmillerw.droids.common.job.impl;

import me.dmillerw.droids.api.IAIController;
import me.dmillerw.droids.common.job.JobDefinition;
import me.dmillerw.droids.common.job.JobInstance;
import me.dmillerw.droids.common.job.parameter.Parameter;
import me.dmillerw.droids.common.job.parameter.ParameterKey;
import me.dmillerw.droids.common.job.parameter.ParameterMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class JobHarvestPlants extends JobDefinition {

    private static final String KEY = "harvest_plants";

    private static final ParameterKey PARAM_AREA = ParameterKey.of(Parameter.Type.AREA, "area");
    private static final ParameterKey PARAM_POS = ParameterKey.of(Parameter.Type.BLOCK_POS, "weenis");

    public JobHarvestPlants() {
        super(KEY, "Harvests all allowed plants/crops from a defined area");

        addParameter(new Parameter(PARAM_AREA, "Start coordinate"));
        addParameter(new Parameter(PARAM_POS, "weenis coordinate"));
    }

    @Override
    public void calculateParameterErrors(ParameterMap parameters, List<String> errors) {
//        if (parameters.isNull(PARAM_AREA))
//            errors.add("Start blockpos must be set");
    }

    @Override
    public JobInstance createInstance() {
        return new JobHarvestPlants.Instance();
    }

    @Override
    public ItemStack getRenderIcon() {
        return new ItemStack(Blocks.RED_FLOWER);
    }

    public static class Instance extends JobInstance {

        @Override
        public void tick(IAIController controller) {
            BlockPos start = parameters.getBlockPos(PARAM_AREA);
        }
    }
}
