package me.dmillerw.droids.common.entity.ai;

import me.dmillerw.droids.common.entity.EntityDroid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.ChunkCache;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.UUID;

public class AIHelper {

    private final double DISTANCE_TOLERANCE = 1.4;

    private final EntityDroid minion;
    private final double speed;

    private PathFinder pathFinder;

    public AIHelper(EntityDroid minion, double speed) {
        this.minion = minion;
        this.speed = speed;

        retrievePathFinder();
    }

    private void retrievePathFinder() {
        try {
            Class<PathNavigate> clazz = PathNavigate.class;
            Field fieldPathFinder = Arrays.stream(clazz.getDeclaredFields())
                    .filter((field) -> PathFinder.class == field.getType())
                    .findAny().orElse(null);

            if (fieldPathFinder == null) {
                throw new IllegalStateException("Couldn't find pathFinder field in PathNavigate.class. Contact the mod author!");
            }

            fieldPathFinder.setAccessible(true);
            pathFinder = (PathFinder) fieldPathFinder.get(minion.getNavigator());
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to get path finder from entity navigator: " + ex.toString());
        }
    }

    public boolean canPathTo(BlockPos pos) {
        return getPathToPosition(pos) != null;
    }

    public boolean moveToPosition(BlockPos pos) {
        return minion.getNavigator().setPath(getPathToPosition(pos), speed);
    }

    public boolean moveToEntity(Entity entity) {
        return minion.getNavigator().setPath(getPathToPosition(entity.getPosition()), speed);
    }

    public boolean pickupItem(EntityItem targetItem) {
        double d = getSquareDist(minion, targetItem);

        moveToEntity(targetItem);

        if (d < DISTANCE_TOLERANCE) {
            if (!targetItem.cannotPickup()) {
                minion.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, targetItem.getItem().copy());
                targetItem.setDead();
                return true;
            }
        }
        return false;
    }

    public boolean getToBlock(BlockPos target) {
        double d = getSquareDist(minion, target);

        moveToPosition(target);

        return d < DISTANCE_TOLERANCE;
    }

    private static double getSquareDist(Entity source, BlockPos dest) {
        double d0 = dest.distanceSqToCenter(source.posX, source.posY - 1, source.posZ);
        double d1 = dest.distanceSqToCenter(source.posX, source.posY, source.posZ);
        double d2 = dest.distanceSqToCenter(source.posX, source.posY + source.getEyeHeight(), source.posZ);
        return Math.min(Math.min(d0, d1), d2);
    }

    private static double getSquareDist(Entity source, Entity dest) {
        Vec3d lowPosition = new Vec3d(source.posX, source.posY - 1, source.posZ);
        Vec3d position = new Vec3d(source.posX, source.posY, source.posZ);
        Vec3d eyePosition = new Vec3d(source.posX, source.posY + source.getEyeHeight(), source.posZ);
        double d0 = lowPosition.squareDistanceTo(dest.posX, dest.posY, dest.posZ);
        double d1 = position.squareDistanceTo(dest.posX, dest.posY, dest.posZ);
        double d2 = eyePosition.squareDistanceTo(dest.posX, dest.posY, dest.posZ);
        return Math.min(Math.min(d0, d1), d2);
    }

    private Path getPathToPosition(BlockPos targetPos) {
        BlockPos minionPos = minion.getPosition();
        int distance = (int)Math.ceil(minionPos.distanceSq(targetPos));
        PathNavigate navigator = minion.getNavigator();
        minion.world.profiler.startSection("pathfind");
        int i = (int) (distance + 8.0F);
        ChunkCache chunkcache = new ChunkCache(minion.world, minionPos.add(-i, -i, -i), minionPos.add(i, i, i), 0);
        Path path = pathFinder.findPath(chunkcache, minion, targetPos, i);
        minion.world.profiler.endSection();
        return path;
    }

    public EntityItem getEntityForId(UUID entityId) {
        return (EntityItem) minion.world.getLoadedEntityList().stream()
                .filter((e) -> e.getUniqueID().equals(entityId))
                .findFirst().orElse(null);
    }
}
