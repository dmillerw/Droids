package me.dmillerw.droids.api;

import me.dmillerw.droids.api.action.BaseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IActionProvider extends INetworkable, IPlayerOwned {

    public World getTileWorld();
    public BlockPos getTilePosition();

    public int getPriority();

    public void onActionCancelled(BaseAction action);
    public void onActionCompleted(BaseAction action);
}
