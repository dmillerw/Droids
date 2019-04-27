package me.dmillerw.droids.common.tile;

import me.dmillerw.droids.api.IActionProvider;
import me.dmillerw.droids.api.action.BaseAction;
import net.minecraft.util.ITickable;

public class TilePowerRouter extends TileBaseComponent implements ITickable, IActionProvider {

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public void onActionCancelled(BaseAction action) {

    }

    @Override
    public void onActionCompleted(BaseAction action) {

    }

    @Override
    public void update() {

    }
}
