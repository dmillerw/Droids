package me.dmillerw.droids.common.handler;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import me.dmillerw.droids.api.zone.BaseZone;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.Constants;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class ZoneHandler extends WorldSavedData {

    private static final String KEY = "droids:ZoneHandler";

    private static Map<Integer, ZoneHandler> instanceMap = Maps.newHashMap();

    public static ZoneHandler get(World world) {
        int dimension = world.provider.getDimension();
        ZoneHandler handler = instanceMap.get(dimension);
        if (handler == null) {
            handler = new ZoneHandler();
            handler.dimension = dimension;
            instanceMap.put(dimension, handler);
        }

        return handler;
    }

    private static void save(ZoneHandler handler) {
        instanceMap.put(handler.dimension, handler);

        World world = DimensionManager.getWorld(handler.dimension);
        world.setData(KEY, handler);
    }

    private int dimension;

    private Map<UUID, BaseZone> zoneMap = Maps.newHashMap();

    public ZoneHandler() {
        super(KEY);
    }

    public ImmutableList<BaseZone> getZones() {
        return ImmutableList.copyOf(zoneMap.values());
    }

    public ImmutableList<BaseZone> getPlayerZones(EntityPlayer player) {
        UUID uuid = player.getGameProfile().getId();
        return ImmutableList.copyOf(zoneMap.values().stream()
                .filter((zone) -> zone.getOwner().equals(uuid))
                .collect(Collectors.toList()));
    }

    public void addZone(BaseZone zone) {
        zoneMap.put(zone.getZoneId(), zone);
    }

    public void removeZone(BaseZone zone) {
        zoneMap.remove(zone.getZoneId());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList list = nbt.getTagList("zones", Constants.NBT.TAG_COMPOUND);
        for (int i=0; i<list.tagCount(); i++) {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            addZone(BaseZone.fromNbt(tag));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagList list = new NBTTagList();

        zoneMap.values().forEach((zone) -> {
            NBTTagCompound tag = new NBTTagCompound();
            zone.writeToNBT(tag);
            list.appendTag(tag);
        });

        return compound;
    }
}
