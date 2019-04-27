package me.dmillerw.droids.common.tile;

import me.dmillerw.droids.api.IActionProvider;
import me.dmillerw.droids.api.action.BaseAction;

public class TileItemRouter extends TileBaseComponent implements IActionProvider  {

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
}
