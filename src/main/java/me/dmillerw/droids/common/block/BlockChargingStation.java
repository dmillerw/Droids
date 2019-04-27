package me.dmillerw.droids.common.block;

import me.dmillerw.droids.common.block.BaseTileBlock;
import me.dmillerw.droids.common.tile.TileChargingStation;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockChargingStation extends BaseTileBlock {

    public static final String NAME = "charging_station";

    protected BlockChargingStation() {
        super(NAME);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileChargingStation();
    }
}