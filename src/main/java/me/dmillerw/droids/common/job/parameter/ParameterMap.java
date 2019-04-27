package me.dmillerw.droids.common.job.parameter;

import com.google.common.collect.Maps;
import me.dmillerw.droids.common.job.JobDefinition;
import me.dmillerw.droids.common.job.JobRegistry;
import me.dmillerw.droids.common.util.type.Area;
import me.dmillerw.droids.common.util.type.BlockFilter;
import me.dmillerw.droids.common.util.type.ItemFilter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

import java.util.Map;

public class ParameterMap {

    public static ParameterMap fromNbt(NBTTagCompound tagCompound) {
        ParameterMap map = new ParameterMap();
        map.readFromNbt(tagCompound);
        return map;
    }

    private static class NullValue {}
    
    private JobDefinition job;
    private final Map<ParameterKey, Object> parameters = Maps.newHashMap();

    public ParameterMap() {}

    public ParameterMap(JobDefinition job) {
        this.job = job;
        
        for (Parameter p : job.getParameters()) {
            this.parameters.put(p.key, new NullValue());
        }
    }

    public boolean isNull(ParameterKey key) {
        return !parameters.containsKey(key) || parameters.get(key) instanceof NullValue;
    }

    private Object getParameter(ParameterKey key) {
        return parameters.get(key);
    }

    public void setParameter(ParameterKey key, Object value) {
        parameters.put(key, value);
    }
    
    public double getNumber(ParameterKey key) {
        Object value = getParameter(key);
        if (value == null)
            throw new IllegalStateException(error(Parameter.Type.NUMBER, key));

        if (value instanceof NullValue)
            return 0;

        return (double) value;
    }

    public BlockPos getBlockPos(ParameterKey key) {
        Object value = getParameter(key);
        if (value == null)
            throw new IllegalStateException(error(Parameter.Type.BLOCK_POS, key));

        if (value instanceof NullValue)
            return BlockPos.ORIGIN;

        return (BlockPos) value;
    }

    public Area getArea(ParameterKey key) {
        Object value = getParameter(key);
        if (value == null)
            throw new IllegalStateException(error(Parameter.Type.AREA, key));

        if (value instanceof NullValue)
            return Area.ORIGIN;

        return (Area) value;
    }

    public ItemFilter getItemFilter(ParameterKey key) {
        Object value = getParameter(key);
        if (value == null)
            throw new IllegalStateException(error(Parameter.Type.ITEM, key));

        if (value instanceof NullValue)
            return ItemFilter.ALLOW_ALL;

        return (ItemFilter) value;
    }

    public BlockFilter getBlockFilter(ParameterKey key) {
        Object value = getParameter(key);
        if (value == null)
            throw new IllegalStateException(error(Parameter.Type.BLOCK, key));

        if (value instanceof NullValue)
            return BlockFilter.ALLOW_ALL;

        return (BlockFilter) value;
    }
    
    private String error(Parameter.Type type, ParameterKey key) {
        return "Tried to read " + type.name() + " parameter named " + key.key + ", but it doesn't exist in job " + job.key;
    }
    
    /* NBT */

    public void writeToNbt(NBTTagCompound tagCompound) {
        tagCompound.setString("jobKey", job.key);

        NBTTagList list = new NBTTagList();

        parameters.forEach((key, value) -> {
            NBTTagCompound paramTag = new NBTTagCompound();
            
            paramTag.setString("key", key.key);
            paramTag.setString("type", key.type.name());

            if (value instanceof NullValue) {
                paramTag.setBoolean("null", true);
            } else {
                paramTag.setBoolean("null", false);

                switch (key.type) {
                    case NUMBER: {
                        paramTag.setDouble("value", (Double) value);
                        break;
                    }

                    case BLOCK_POS: {
                        paramTag.setLong("value", ((BlockPos)value).toLong());
                        break;
                    }

                    case AREA: {
                        paramTag.setTag("value", ((Area)value).toNbt());
                        break;
                    }

                    case ITEM: {
                        NBTTagCompound subtag = new NBTTagCompound();
                        ((ItemFilter)value).writeToNbt(subtag);

                        paramTag.setTag("value", subtag);

                        break;
                    }

                    case BLOCK: {
                        NBTTagCompound subtag = new NBTTagCompound();
                        ((BlockFilter)value).writeToNbt(subtag);

                        paramTag.setTag("value", subtag);

                        break;
                    }

                    default: break;
                }
            }


            list.appendTag(paramTag);
        });

        tagCompound.setTag("values", list);

        System.out.println("WRITE: " + tagCompound.toString());
    }
    
    public void readFromNbt(NBTTagCompound tagCompound) {
        System.out.println("READ: " + tagCompound.toString());

        job = JobRegistry.INSTANCE.getJob(tagCompound.getString("jobKey"));

        NBTTagList list = tagCompound.getTagList("values", Constants.NBT.TAG_COMPOUND);

        for (int i=0; i<list.tagCount(); i++) {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            Parameter.Type type = Parameter.Type.fromString(tag.getString("type"));
            String key = tag.getString("key");

            ParameterKey pk = ParameterKey.of(type, key);

            if (tag.getBoolean("null")) {
                setParameter(pk, new NullValue());
            } else {
                switch (type) {
                    case NUMBER: {
                        setParameter(pk, tag.getDouble("value"));
                        break;
                    }

                    case BLOCK_POS: {
                        setParameter(pk, BlockPos.fromLong(tag.getLong("value")));
                        break;
                    }

                    case AREA: {
                        setParameter(pk, Area.fromNbt(tag.getCompoundTag("value")));
                        break;
                    }

                    case ITEM: {
                        setParameter(pk, ItemFilter.fromNbt(tag.getCompoundTag("value")));
                        break;
                    }

                    case BLOCK: {
                        setParameter(pk, BlockFilter.fromNbt(tag.getCompoundTag("value")));
                        break;
                    }

                    default: break;
                }
            }

        }
    }
}
