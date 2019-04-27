package me.dmillerw.droids.common.job;

import me.dmillerw.droids.api.IAIController;
import me.dmillerw.droids.common.job.parameter.ParameterMap;
import net.minecraft.nbt.NBTTagCompound;

public abstract class JobInstance {

    protected ParameterMap parameters;

    public final void setParameters(ParameterMap parameters) {
        this.parameters = parameters;
    }

    public void readFromNbt(NBTTagCompound tag) {}

    public void writeToNbt(NBTTagCompound tag) {}

    public abstract void tick(IAIController controller);
}
