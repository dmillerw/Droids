package me.dmillerw.droids.common.tile;

import me.dmillerw.droids.api.action.BaseAction;

public class TileChargingStation extends TileBaseComponent {


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
