package me.dmillerw.droids.common.block;

import me.dmillerw.droids.common.tile.TileAIController;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Every second (20 ticks), the AI controller first iterates through its network, and gets the latest list create required
 * tasks from every connected controller.
 *
 * New tasks are assigned to available droids, the rest simply sit until a droid is available to work on them
 *
 * Deleted tasks are marked as such, with the droid implementing whatever cleanup processes are necessary until
 * complete
 *
 * Finally, the AI controller will update the status create any tasks that have changed, and provide that information
 * back to the controllers
 */
public class BlockAIController extends BaseTileBlock {

    public static final String NAME = "ai_controller";

    protected BlockAIController() {
        super(NAME);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileAIController();
    }
}
