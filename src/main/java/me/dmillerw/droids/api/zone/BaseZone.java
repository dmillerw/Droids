package me.dmillerw.droids.api.zone;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.UUID;

public class BaseZone {

    public static BaseZone fromNbt(NBTTagCompound tag) {
        String zoneType = tag.getString("zoneType");

        BaseZone zone = ZoneFactory.newInstance(zoneType);
        zone.readFromNBT(tag);

        return zone;
    }

    private final String zoneType;

    private UUID zoneId;
    private UUID ownerId;

    private AxisAlignedBB aabb;

    public BaseZone(String zoneType) {
        this.zoneType = zoneType;
    }

    public final UUID getOwner() {
        return ownerId;
    }

    public final UUID getZoneId() {
        return zoneId;
    }

    public AxisAlignedBB getBounds() {
        return aabb;
    }

    protected final void setBounds(AxisAlignedBB aabb) {
        this.aabb = aabb;
    }

    public void writeToNBT(NBTTagCompound tag) {
        tag.setString("zoneType", zoneType);

        tag.setUniqueId("zoneId", zoneId);

        tag.setUniqueId("ownerId", ownerId);

        tag.setDouble("aabb_minx", aabb.minX);
        tag.setDouble("aabb_miny", aabb.minY);
        tag.setDouble("aabb_minz", aabb.minZ);

        tag.setDouble("aabb_maxx", aabb.maxX);
        tag.setDouble("aabb_maxy", aabb.maxY);
        tag.setDouble("aabb_maxz", aabb.maxZ);
    }

    public void readFromNBT(NBTTagCompound tag) {
        this.zoneId = tag.getUniqueId("zoneId");
        this.ownerId = tag.getUniqueId("ownerId");

        double minX = tag.getDouble("aabb_minx");
        double minY = tag.getDouble("aabb_miny");
        double minZ = tag.getDouble("aabb_minz");
        double maxX = tag.getDouble("aabb_maxx");
        double maxY = tag.getDouble("aabb_maxy");
        double maxZ = tag.getDouble("aabb_maxz");
        this.aabb = new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    /* IMPLEMENTATION STUFF */

    public ItemStack getIcon() {
        return ItemStack.EMPTY;
    }

    public int getColor() {
        return 0xFFFFFFFF;
    }

    public void onPlayerInteractWith(World world, EntityPlayer player) {}
}
