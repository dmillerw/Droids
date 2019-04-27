package me.dmillerw.droids.common.block;

import me.dmillerw.droids.common.ModInfo;
import me.dmillerw.droids.common.tile.TileCore;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BaseTileBlock extends BlockContainer {

    protected BaseTileBlock(String name) {
        super(Material.IRON);

        setHardness(2F);
        setResistance(2F);

        setCreativeTab(ModInfo.TAB);

        setUnlocalizedName(name);
        setRegistryName(new ResourceLocation(ModInfo.ID, name));
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileCore tile = (TileCore) worldIn.getTileEntity(pos);
        if (tile != null)
            tile.onBlockBreak();

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    public abstract TileEntity createNewTileEntity(World world, int meta);
}
