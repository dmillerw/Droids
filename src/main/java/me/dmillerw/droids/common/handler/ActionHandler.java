package me.dmillerw.droids.common.handler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.dmillerw.droids.api.IActionProvider;
import me.dmillerw.droids.api.action.BaseAction;
import me.dmillerw.droids.common.entity.EntityDroid;
import me.dmillerw.droids.common.util.NBTUtils;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class ActionHandler extends WorldSavedData {

    private static class EntityKey {

        private final UUID uuid;

        private EntityKey(Entity entity) {
            this.uuid = entity.getPersistentID();
        }

        public UUID getEntity() {
            return uuid;
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

    private static class BlockKey {

        private final BlockPos target;

        private BlockKey(BlockPos target) {
            this.target = target;
        }

        public BlockPos getTarget() {
            return target;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BlockKey blockKey = (BlockKey) o;
            return Objects.equals(target, blockKey.target);
        }

        @Override
        public int hashCode() {
            return Objects.hash(target);
        }
    }
    
    private static final String KEY = "droids:ActionHandler";

    private static Map<Integer, ActionHandler> instanceMap = Maps.newHashMap();

    public static ActionHandler get(World world) {
        int dimension = world.provider.getDimension();
        ActionHandler handler = instanceMap.get(dimension);
        if (handler == null) {
            handler = new ActionHandler();
            handler.setDimension(dimension);
            instanceMap.put(dimension, handler);
        }

        return handler;
    }

    private static void save(ActionHandler handler) {
        instanceMap.put(handler.dimension, handler);

        World world = DimensionManager.getWorld(handler.dimension);
        world.setData(KEY, handler);
    }

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        int dimension = event.getWorld().provider.getDimension();

        ActionHandler handler = (ActionHandler) event.getWorld().loadData(ActionHandler.class, KEY);
        if (handler == null) {
            handler = new ActionHandler();
            handler.setDimension(dimension);

            save(handler);
        }
    }

    private int dimension;

    public ActionHandler() {
        super(KEY);
    }

    private Map<Integer, BaseAction> actionMap = Maps.newHashMap();

    private Map<EntityKey, List<Integer>> entityToActionMap = Maps.newHashMap();
    private Map<BlockKey, List<Integer>> blockToActionMap = Maps.newHashMap();

    private Map<Integer, EntityKey> actionToEntityMap = Maps.newHashMap();
    private Map<Integer, BlockKey> actionToBlockMap = Maps.newHashMap();

    private int nextId = 0;

    public BaseAction getAction(int id) {
        return actionMap.get(id);
    }

//    public void deleteEntityAction(int id) {
//        actionMap.remove(id);
//        entityToActionMap.get(actionToEntityMap.get(id)).remove(id);
//        actionToEntityMap.remove(id);
//    }
//
//    public void deleteBlockAction(int id) {
//        actionMap.remove(id);
//        blockToActionMap.get(actionToBlockMap.get(id)).remove(id);
//        actionToEntityMap.remove(id);
//    }

    public void updateAction(BaseAction action) {
        actionMap.put(action.getActionId(), action);
        if (action.getProgress() == BaseAction.Progress.COMPLETE)
            markActionAsComplete(action);
        else if (action.getProgress() == BaseAction.Progress.CANCELLED)
            markActionAsCancelled(action, action.getCancelReason());

        save();
    }

    private void markActionAsComplete(BaseAction action) {
        action.markAsComplete();

        TileEntity tile = getWorld().getTileEntity(action.getProviderOrigin());
        if (tile instanceof IActionProvider)
            ((IActionProvider) tile).onActionCompleted(action);

        deleteAction(action, true);
    }

    private void markActionAsCancelled(BaseAction action, BaseAction.CancelReason cancelReason) {
        action.markAsCancelled(cancelReason);

        TileEntity tile = getWorld().getTileEntity(action.getProviderOrigin());
        if (tile instanceof IActionProvider)
            ((IActionProvider) tile).onActionCompleted(action);

        deleteAction(action, true);
    }

    private void deleteAction(BaseAction action, boolean consume) {
        int id = action.getActionId();

        actionMap.remove(id);

        EntityKey entityKey = actionToEntityMap.get(id);
        if (entityKey != null) {
            actionToEntityMap.remove(id);
            if (consume)
                entityToActionMap.remove(entityKey);

            save();

            return;
        }

        BlockKey blockKey = actionToBlockMap.get(id);
        if (blockKey != null) {
            actionToBlockMap.remove(id);
            if (consume)
                blockToActionMap.remove(blockKey);

            save();

            return;
        }
    }

    public BaseAction getClosestNextAction(EntityDroid droid) {
        BaseAction action = null;
        int distance = Integer.MAX_VALUE;

        for (BaseAction a : actionMap.values()) {
            if (a.getProgress() != BaseAction.Progress.INIITIAL || a.getDroidId() != null)
                continue;

            int d = a.getDistanceToDroid(droid);

            if (d < distance) {
                action = a;
                distance = d;
            }
        }

        return action;
    }

    public BaseAction getNextAction(EntityKey entity) {
        List<Integer> actions = entityToActionMap.get(entity);
        if (actions == null || actions.isEmpty())
            return null;

        for (int i : actions) {
            BaseAction action = actionMap.get(i);
            if (action.getProgress() != BaseAction.Progress.INIITIAL || action.getDroidId() != null)
                continue;

            return action;
        }

        return null;
    }

    public BaseAction getNextAction(BlockKey block) {
        List<Integer> actions = blockToActionMap.get(block);
        if (actions == null || actions.isEmpty())
            return null;

        for (int i : actions) {
            BaseAction action = actionMap.get(i);
            if (action.getProgress() != BaseAction.Progress.INIITIAL || action.getDroidId() != null)
                continue;

            return action;
        }

        return null;
    }

    public void queueEntityAction(Entity entity, BaseAction action) {
        int id = nextId();
        action.setActionId(id);

        final ActionHandler.EntityKey key = new ActionHandler.EntityKey(entity);

//        if (isEntityClaimed(action.getProviderOrigin(), key))
//            return;

        List<Integer> list = entityToActionMap.get(key);
        if (list == null) list = Lists.newArrayList();

        list.add(id);

//        Set<BlockPos> providers = providersUsingEntities.get(key);
//        if (providers == null) providers = Sets.newHashSet();
//        providers.add(action.getProviderOrigin());
//        providersUsingEntities.put(key, providers);

        actionMap.put(id, action);
        entityToActionMap.put(key, list);

        save();
    }

    public void queueBlockAction(BlockPos block, BaseAction action) {
        int id = nextId();
        action.setActionId(id);

        final BlockKey key = new BlockKey(block);

//        if (isBlockClaimed(action.getProviderOrigin(), key))
//            return;

        List<Integer> list = blockToActionMap.get(block);
        if (list == null) list = Lists.newArrayList();

        list.add(id);

//        Set<BlockPos> providers = providersUsingBlocks.get(key);
//        if (providers == null) providers = Sets.newHashSet();
//        providers.add(action.getProviderOrigin());
//        providersUsingBlocks.put(key, providers);

        actionMap.put(id, action);
        blockToActionMap.put(key, list);

        save();
    }

//    public boolean isEntityClaimed(BlockPos provider, EntityKey key) {
//        Set<BlockPos> claimed = providersUsingEntities.get(key);
//        if (claimed == null || claimed.isEmpty())
//            return false;
//
//        return claimed.contains(provider);
//    }

//    public boolean isBlockClaimed(BlockPos provider, BlockKey key) {
//        Set<BlockPos> claimed = providersUsingBlocks.get(key);
//        if (claimed == null || claimed.isEmpty())
//            return false;
//
//        return claimed.contains(provider);
//    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.dimension = nbt.getInteger("dimension");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        return NBTUtils.tagBuilder(compound).addInt("dimension", dimension).build();
    }

    public void save() {
        instanceMap.put(this.dimension, this);
    }

    private int nextId() {
        nextId++;
        save();
        return nextId;
    }

    private World getWorld() {
        return DimensionManager.getWorld(this.dimension);
    }
}
