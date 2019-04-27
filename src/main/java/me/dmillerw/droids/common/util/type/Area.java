package me.dmillerw.droids.common.util.type;

import io.netty.buffer.ByteBuf;
import me.dmillerw.droids.common.util.NBTUtils;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class Area {

    public static final Area ORIGIN = new Area(BlockPos.ORIGIN, BlockPos.ORIGIN);

    public static Area fromBuffer(ByteBuf buffer) {
        return new Area(BlockPos.fromLong(buffer.readLong()), BlockPos.fromLong(buffer.readLong()));
    }

    public static Area fromNbt(NBTTagCompound tagCompound) {
        return new Area(BlockPos.fromLong(tagCompound.getLong("start")), BlockPos.fromLong(tagCompound.getLong("end")));
    }
    public final BlockPos startPos;

    public final BlockPos endPos;

    private final BlockPos center;

    public Area(BlockPos startPos, BlockPos endPos) {
        this.startPos = new BlockPos(Math.min(startPos.getX(), endPos.getX()), Math.min(startPos.getY(), endPos.getY()), Math.min(startPos.getZ(), endPos.getZ()));
        this.endPos = new BlockPos(Math.max(startPos.getX(), endPos.getX()), Math.max(startPos.getY(), endPos.getY()), Math.max(startPos.getZ(), endPos.getZ()));
        this.center = this.endPos.subtract(this.startPos);
    }

    public BlockPos getCenter() {
        BlockPos diff = this.endPos.subtract(this.startPos);
        BlockPos halfway = new BlockPos(diff.getX() / 2, diff.getY() / 2, diff.getZ() / 2);
        return this.startPos.add(halfway);
    }

    public <T extends Entity> List<T> getEntities(World world, Class<T> type) {
        return world.getEntitiesWithinAABB(type, new AxisAlignedBB(startPos, endPos).expand(1, 1, 1));
    }

    public Iterable<BlockPos> getBlocksInside() {
        return BlockPos.getAllInBox(startPos, endPos);
    }

    public boolean isEntityInsideOf(Entity entity) {
        return (entity.posX >= startPos.getX() &&
                entity.posZ >= startPos.getZ() &&
                entity.posX <= endPos.getX() &&
                entity.posZ <= endPos.getZ());
    }

    public void writeToBuffer(ByteBuf buffer) {
        buffer.writeLong(startPos.toLong());
        buffer.writeLong(endPos.toLong());
    }

    public NBTTagCompound toNbt() {
        return NBTUtils.tagBuilder()
                .addLong("start", startPos.toLong())
                .addLong("end", endPos.toLong())
                .build();
    }
}
