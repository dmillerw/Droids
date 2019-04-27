package me.dmillerw.droids.common.block;

import me.dmillerw.droids.common.tile.TileFluidRouter;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockFluidRouter extends BaseTileBlock {

    public static final String NAME = "fluid_router";

    protected BlockFluidRouter() {
        super(NAME);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileFluidRouter();
    }
}