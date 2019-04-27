package me.dmillerw.droids.common.tile;

import me.dmillerw.droids.api.IAIController;
import me.dmillerw.droids.api.IActionProvider;
import me.dmillerw.droids.api.IPlayerOwned;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public abstract class TileBaseComponent extends TileCore implements IActionProvider {

    protected static final String KEY_CONNECTED = "connected";

    private UUID owner;
    private IAIController controller;

    public boolean clientIsNetworkConnected = false;

    @Override
    public void onLoad() {
        if (!world.isRemote) {
            TileAIController.updateNetworks(this);
        }
    }

    @Override
    public void onBlockBreak() {
        if (!world.isRemote) {
            this.tileEntityInvalid = true;
            if (controller != null) {
                controller.markNetworkDirty();
                controller = null;
            }
        }
    }

    @Override
    public void invalidate() {
        if (!world.isRemote) {
            if (controller != null) {
                controller.markNetworkDirty();
                controller = null;
            }
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
    }

    @Override
    public void readFromDisk(NBTTagCompound compound) {
        super.readFromDisk(compound);

        owner = new UUID(compound.getLong("owner_most"), compound.getLong("owner_least"));
    }

    @Override
    public void writeDescription(NBTTagCompound compound) {
        super.writeDescription(compound);

        compound.setBoolean(KEY_CONNECTED, controller != null);
    }

    @Override
    public void readDescription(NBTTagCompound compound) {
        super.readDescription(compound);

        clientIsNetworkConnected = compound.getBoolean(KEY_CONNECTED);
    }

    /* GET SET */

    @Override
    public void setController(IAIController controller) {
        this.controller = controller;
    }

    @Override
    public IAIController getController() {
        return controller;
    }

    /* UTIL */

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    @Override
    public boolean canConnect(IPlayerOwned other) {
        return true;
    }
}
