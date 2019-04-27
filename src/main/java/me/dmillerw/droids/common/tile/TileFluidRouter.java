package me.dmillerw.droids.common.tile;

import com.google.common.collect.Sets;
import me.dmillerw.droids.api.IActionProvider;
import me.dmillerw.droids.api.action.ActionBreakBlock;
import me.dmillerw.droids.api.action.BaseAction;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

import java.util.Set;

public class TileFluidRouter extends TileBaseComponent implements IActionProvider, ITickable {

    private Set<BlockPos> processing = Sets.newHashSet();

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
        if (world.isRemote)
            return;

        if (getController() == null)
            return;

        if (world.getTotalWorldTime() % 20 != 0)
            return;

        int range = 4;
        BlockPos start = pos.add(-range, -range, -range);
        BlockPos end = pos.add(range, range, range);

        BlockPos.getAllInBox(start, end).forEach((position) -> {
            if (world.canBlockSeeSky(position) && !world.isAirBlock(position) && !processing.contains(position)) {
                getController().queueBlockClaim(position, new ActionBreakBlock(position));
                processing.add(position);
            }
        });
    }
}
