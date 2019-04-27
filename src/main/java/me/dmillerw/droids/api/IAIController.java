package me.dmillerw.droids.api;

import me.dmillerw.droids.api.action.BaseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IAIController extends IPlayerOwned {

    public World getTileWorld();
    public BlockPos getTilePosition();

    public boolean isShutdown();

    public void markNetworkDirty();
    public void shutdown();

    public void queueBlockClaim(BlockPos block, BaseAction action);
}
