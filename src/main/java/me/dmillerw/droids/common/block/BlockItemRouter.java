package me.dmillerw.droids.common.block;

import me.dmillerw.droids.common.tile.TileItemRouter;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockItemRouter extends BaseTileBlock {

    public static final String NAME = "item_router";

    protected BlockItemRouter() {
        super(NAME);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileItemRouter();
    }
}