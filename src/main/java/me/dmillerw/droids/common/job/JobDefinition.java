package me.dmillerw.droids.common.job;

import com.google.common.collect.Maps;
import me.dmillerw.droids.common.job.parameter.Parameter;
import me.dmillerw.droids.common.job.parameter.ParameterKey;
import me.dmillerw.droids.common.job.parameter.ParameterMap;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class JobDefinition {

    public final String key;
    public final String description;

    private final Map<ParameterKey, Parameter> parameters = Maps.newHashMap();

    private ItemStack renderIcon;

    public JobDefinition(String key, String description) {
        this.key = key;
        this.description = description;
    }

    public final List<Parameter> getParameters() {
        return new ArrayList<>(parameters.values());
    }

    protected final void addParameter(Parameter parameter) {
        this.parameters.put(parameter.key, parameter);
    }

    public final ParameterMap getBlankParameterMap() {
        return new ParameterMap(this);
    }

    public void calculateParameterErrors(ParameterMap parameters, List<String> errors) {}

    public abstract JobInstance createInstance();

    /* CLIFENT */

    @SideOnly(Side.CLIENT)
    public final ItemStack getIcon() {
        if (renderIcon == null)
            renderIcon = getRenderIcon();
        return renderIcon;
    }

    @SideOnly(Side.CLIENT)
    protected abstract ItemStack getRenderIcon();
}
