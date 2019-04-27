package me.dmillerw.droids.api;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.dmillerw.droids.api.action.BaseAction;
import me.dmillerw.droids.common.entity.EntityDroid;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class ClaimedObjects {

    private static final Map<Integer, ClaimedObjects> claimedObjectsByWorld = Maps.newConcurrentMap();

    public static ClaimedObjects get(World world) {
        ClaimedObjects claimedObjects = claimedObjectsByWorld.get(world.provider.getDimension());
        if (claimedObjects == null) claimedObjects = new ClaimedObjects(world);
        return claimedObjects;
    }

    public static void set(World world, ClaimedObjects claimedObjects) {
        claimedObjectsByWorld.put(world.provider.getDimension(), claimedObjects);
    }

    private static class EntityKey {

        private final UUID uuid;
        private final Entity entity;

        private EntityKey(Entity entity) {
            this.uuid = entity.getPersistentID();
            this.entity = entity;
        }

        public Entity getEntity() {
            return entity;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            EntityKey entityKey = (EntityKey) o;
            return Objects.equals(uuid, entityKey.uuid);
        }

        @Override
        public int hashCode() {
            return Objects.hash(uuid);
        }
    }

    private static class InventoryKey {

        private final BlockPos inventoryPosition;
        private final ItemStack targetItem;

        private InventoryKey(BlockPos inventoryPosition, ItemStack targetItem) {
            this.inventoryPosition = inventoryPosition;
            this.targetItem = targetItem;
        }

        public BlockPos getInventoryPosition() {
            return inventoryPosition;
        }

        public ItemStack getTargetItem() {
            return targetItem;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            InventoryKey other = (InventoryKey) o;
            return Objects.equals(inventoryPosition, other.inventoryPosition) && ItemStack.areItemStacksEqual(targetItem, other.targetItem);
        }
    }

    private final World world;

    private Map<EntityKey, List<BaseAction>> pendingEntityActions = Maps.newHashMap();
    private Map<BlockPos, List<BaseAction>> pendingBlockActions = Maps.newHashMap();

    public ClaimedObjects(World world) {
        this.world = world;
    }

    public EntityKey getClosestEntity(EntityDroid droid) {
        EntityKey key = null;
        float distance = Float.MAX_VALUE;

        for (EntityKey k : pendingEntityActions.keySet()) {
            float f = droid.getDistance(k.entity);
            if (f < distance) {
                key = k;
                distance = f;
            }
        }

        return key;
    }

    public BlockPos getClosestBlock(EntityDroid droid) {
        BlockPos block = null;
        float distance = Float.MAX_VALUE;

        for (BlockPos b : pendingBlockActions.keySet()) {
            float f = (float) droid.getPosition().distanceSq(b);
            if (f < distance) {
                block = b;
                distance = f;
            }
        }

        return block;
    }

    public BaseAction getNextEntityAction(Entity entity, boolean pop) {
        final EntityKey key = new EntityKey(entity);

        List<BaseAction> list = pendingEntityActions.get(key);
        if (list == null || list.isEmpty())
            return null;

        BaseAction action = list.get(0);

        if (pop) {
            list.remove(0);
            pendingEntityActions.put(key, list);
        }

        return action;
    }

    public BaseAction getNextBlockAction(BlockPos block, boolean pop) {
        List<BaseAction> list = pendingBlockActions.get(block);
        if (list == null || list.isEmpty())
            return null;

        BaseAction action = list.get(0);

        if (pop) {
            list.remove(0);
            pendingBlockActions.put(block, list);
        }

        return action;
    }

    public void queueEntityAction(Entity entity, BaseAction action) {
        final EntityKey key = new EntityKey(entity);

        List<BaseAction> list = pendingEntityActions.get(key);
        if (list == null) list = Lists.newArrayList();

        if (providerAlreadyRegistered(list, action))
            return;

        list.add(action);

        pendingEntityActions.put(key, list);
    }

    public void queueBlockAction(BlockPos block, BaseAction action) {
        List<BaseAction> list = pendingBlockActions.get(block);
        if (list == null) list = Lists.newArrayList();

        if (providerAlreadyRegistered(list, action))
            return;

        list.add(action);

        pendingBlockActions.put(block, list);
    }

    private boolean providerAlreadyRegistered(List<BaseAction> list, BaseAction action) {
        return list.stream().anyMatch((a) -> a.getProviderOrigin().equals(action.getProviderOrigin()));
    }

    public void save() {
        ClaimedObjects.set(this.world, this);
    }
}
