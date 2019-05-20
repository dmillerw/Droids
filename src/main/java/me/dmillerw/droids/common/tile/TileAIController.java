package me.dmillerw.droids.common.tile;

import com.google.common.collect.Sets;
import me.dmillerw.droids.api.IAIController;
import me.dmillerw.droids.api.IActionProvider;
import me.dmillerw.droids.api.INetworkable;
import me.dmillerw.droids.api.IPlayerOwned;
import me.dmillerw.droids.api.action.BaseAction;
import me.dmillerw.droids.common.handler.ActionHandler;
import me.dmillerw.droids.common.entity.EntityDroid;
import me.dmillerw.droids.common.item.ItemUpgradeCard;
import me.dmillerw.droids.common.util.PathFinder;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class TileAIController extends TileCore implements IAIController, IPlayerOwned, ITickable {

    private static final int PENDING_ACTIONS_PER_TICK = 10;
    private static final int UPGRADE_SLOTS = 9;

    public static void updateNetworks(IActionProvider provider) {
        updateNetworks(provider, provider.getTileWorld(), provider.getTilePosition());
    }

    private static void updateNetworks(IPlayerOwned owner, World worldIn, BlockPos posIn) {
        System.out.println("Updating networks around: " + posIn);

        PathFinder pathFinder = new PathFinder(worldIn, posIn);
        pathFinder.find(true, (world, pos) -> {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof IPlayerOwned) {
                if (!(((IPlayerOwned) tile).canConnect(owner))) {
                    return false;
                }
            }

            return tile instanceof IAIController || tile instanceof INetworkable;
        });

        Set<BlockPos> positions = pathFinder.getConnectedBlocks();

        for (BlockPos pos : positions) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof IAIController) {
                System.out.println("Found network at " + ((IAIController) tile).getTilePosition());
                ((IAIController) tile).markNetworkDirty();
            }
        }
    }

    private UUID owner;
    private NonNullList<ItemStack> upgrades = NonNullList.withSize(UPGRADE_SLOTS, ItemStack.EMPTY);

    private Set<BlockPos> coverage = Sets.newHashSet();
    private Set<IActionProvider> cachedProviders = Sets.newHashSet();

    private int currentRange;

    private boolean shutdown = false;

    private boolean initialized = false;
    private boolean networkDirty = false;

    @Override
    public void onLoad() {
        if (!world.isRemote) {
            initialize();
            initialized = true;
        }
    }

    @Override
    public void invalidate() {
        if (!world.isRemote) {
            destroy();

            updateNetworks(this, world, pos);
        }

        super.invalidate();
    }

    @Override
    public World getTileWorld() {
        return world;
    }

    @Override
    public BlockPos getTilePosition() {
        return pos;
    }

    @Override
    public UUID getOwner() {
        return owner;
    }

    /* NBT */

    @Override
    public void writeToDisk(NBTTagCompound compound) {
        super.writeToDisk(compound);

        compound.setLong("owner_most", owner.getMostSignificantBits());
        compound.setLong("owner_least", owner.getLeastSignificantBits());

        ItemStackHelper.saveAllItems(compound, upgrades, true);
    }

    @Override
    public void readFromDisk(NBTTagCompound compound) {
        super.readFromDisk(compound);

        owner = new UUID(compound.getLong("owner_most"), compound.getLong("owner_least"));

        upgrades = NonNullList.withSize(UPGRADE_SLOTS, ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, upgrades);

//        update();
    }

    /* FUNC */

    @Override
    public void update() {
        if (world.isRemote)
            return;

        if (world.getTotalWorldTime() % 20 != 0)
            return;

        if (networkDirty) {
            shutdown = false;

            updateProviders();
            networkDirty = false;
        }

        if (shutdown)
            return;

        ActionHandler handler = ActionHandler.get(world);

        List<EntityDroid> droids = world.getEntities(EntityDroid.class, (droid) -> droid != null && droid.getDistanceSq(this.pos) <= 32);
        for (EntityDroid droid : droids) {
            if (droid.getAction() != -1)
                continue;

            BaseAction action = handler.getClosestNextAction(droid);
            if (action == null)
                continue;

            action.markAsInProgress();
            action.setDroidId(droid.getUniqueID());

            droid.setAction(action.getActionId());

            handler.updateAction(action);
        }
    }

    private void initialize() {
        if (world.isRemote)
            return;

        if (initialized)
            return;

        currentRange = getRange();

        System.out.println("Initializing TileAIController @ " + pos);

        updateCoverage();
        updateProviders();
    }

    private void destroy() {
        System.out.println("Destroying TileAIController @ " + pos);

        coverage.clear();
        cachedProviders.forEach((provider -> provider.setController(null)));
        cachedProviders.clear();
    }

    private void updateCoverage() {
        BlockPos start = getTilePosition().add(-currentRange, -currentRange, -currentRange);
        BlockPos end = getTilePosition().add(currentRange, currentRange, currentRange);

        coverage.clear();

        BlockPos.getAllInBox(start, end).forEach(coverage::add);

        System.out.println("Coverage updated to span " + coverage.size() + " blocks");
    }

    private void updateProviders() {
        PathFinder pathFinder = new PathFinder(world, pos);
        pathFinder.find(true, (world, pos) -> {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof IPlayerOwned) {
                if (!(((IPlayerOwned) tile).canConnect(this))) {
                    return false;
                }
            }

            return tile instanceof IAIController || tile instanceof INetworkable;
        });

        Set<BlockPos> connected = pathFinder.getConnectedBlocks();
        Set<IActionProvider> temp = Sets.newHashSet();

        System.out.println("Updating providers: Found " + connected.size() + " connected blocks");

        for (BlockPos position : connected) {
            TileEntity tile = world.getTileEntity(position);
            if (tile instanceof IAIController) {
                if (position.equals(this.pos))
                    continue;

                System.out.println("Found another controller @ " + tile.getPos() + " while updating, shutting down...");

                temp.forEach((provider -> provider.setController(null)));
                temp.clear();

                shutdown();
                ((IAIController) tile).shutdown();

                markDirtyAndNotify();

                return;
            }

            if (tile instanceof IActionProvider) {
                ((IActionProvider) tile).setController(this);
                System.out.println("Added provider: " + ((IActionProvider) tile).getTilePosition());
                temp.add((IActionProvider) tile);
            }
        }

        for (IActionProvider provider : cachedProviders) {
            if (!connected.contains(provider.getTilePosition()) && provider.getController() != null) {
                System.out.println("Removed provider: " + provider.getTilePosition());
                provider.setController(null);
            }
        }

        cachedProviders.clear();
        cachedProviders.addAll(temp);

        System.out.println("Update complete, now have " + cachedProviders.size() + " providers");
    }

    /* GET SET */

    public void setOwner(UUID uuid) {
        this.owner = uuid;
    }

    @Override
    public boolean canConnect(IPlayerOwned other) {
//        if (getOwner() == null || other == null || other.getOwner() == null)
//            return false;
//
//        return getOwner().equals(other.getOwner());
        return true;
    }

    @Override
    public boolean isShutdown() {
        return shutdown;
    }

    public void markNetworkDirty() {
        this.networkDirty = true;
    }

    @Override
    public void shutdown() {
        this.shutdown = true;

        destroy();
    }

    @Override
    public void queueBlockClaim(BlockPos block, BaseAction action) {
        action.setProviderOrigin(getTilePosition());

        ActionHandler handler = ActionHandler.get(world);
        handler.queueBlockAction(block, action);
        handler.save();
    }

    /* UTIL */

    private int getRange() {
        int range = 0;

        for (ItemStack stack : upgrades) {
            ItemUpgradeCard.Upgrade upgrade = ItemUpgradeCard.getUpgrade(stack);
            if (upgrade == null)
                continue;

            if (upgrade.type == ItemUpgradeCard.UpgradeType.RANGE) {
                range += ItemUpgradeCard.UpgradeType.RANGE.baseValue * upgrade.modifier;
            }
        }

        return range;
    }
}
