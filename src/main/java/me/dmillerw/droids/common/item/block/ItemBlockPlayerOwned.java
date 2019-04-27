package me.dmillerw.droids.common.item.block;

import me.dmillerw.droids.api.IPlayerOwned;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlockPlayerOwned extends ItemBlock {

    public ItemBlockPlayerOwned(Block block) {
        super(block);
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
        boolean result = super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);
        if (result) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof IPlayerOwned)
                ((IPlayerOwned) tile).setOwner(player.getGameProfile().getId());
        }

        return result;
    }
}
