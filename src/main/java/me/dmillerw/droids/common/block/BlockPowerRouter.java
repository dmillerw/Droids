package me.dmillerw.droids.common.block;

import me.dmillerw.droids.common.tile.TilePowerRouter;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockPowerRouter extends BaseTileBlock {

    public static final String NAME = "power_router";

    protected BlockPowerRouter() {
        super(NAME);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TilePowerRouter();
    }
}