package me.dmillerw.droids.common.item;

import me.dmillerw.droids.common.ModInfo;
import me.dmillerw.droids.common.entity.EntityDroid;
import me.dmillerw.flow.modal.GuiModalLayout;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ItemConfigurator extends BaseItem {

    public static final String KEY = "configurator";

    public static boolean debugPauseDroidTicks = false;

    public ItemConfigurator() {
        super(KEY);

        setMaxStackSize(1);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        if (playerIn.world.isRemote)
            return true;

        if (playerIn.isSneaking()) {
            if (!(target instanceof EntityDroid))
                return false;

            playerIn.sendMessage(new TextComponentString("DROID ID: " + target.getUniqueID()));
            playerIn.sendMessage(new TextComponentString("SKIN: " + ((EntityDroid) target).getSkin()));
            playerIn.sendMessage(new TextComponentString("ACTION: " + ((EntityDroid) target).getAction()));

            return true;
        }
        return false;
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        /*if (world.isRemote)
            return EnumActionResult.FAIL;

        if (player.isSneaking()) {
            TileEntity tile = world.getTileEntity(pos);

            if (tile instanceof IPlayerOwned) {
                player.sendMessage(new TextComponentString("OWNER: " + ((IPlayerOwned) tile).getOwner()));
            }

            if (tile instanceof INetworkable) {
                player.sendMessage(new TextComponentString("CONTROLLER: " + ((INetworkable) tile).getController()));
            }

            if (tile instanceof IAIController) {
                player.sendMessage(new TextComponentString("ERRORED: " + ((IAIController) tile).isShutdown()));
            }

            player.sendMessage(new TextComponentString(""));

            return EnumActionResult.SUCCESS;
        } else {
            debugPauseDroidTicks = !debugPauseDroidTicks;
            player.sendMessage(new TextComponentString("TICK PAUSE: " + debugPauseDroidTicks));
        }

        return EnumActionResult.FAIL;*/

//        if (player.isSneaking()) {
//            player.openGui(Droids.INSTANCE, GuiHandler.Gui.CONFIGURATOR.ordinal(), world, 0, 0, 0);
//            return EnumActionResult.SUCCESS;
//        }

        Minecraft.getMinecraft().displayGuiScreen(new GuiModalLayout(new ResourceLocation(ModInfo.ID, "textures/gui/modal.png")));

        return EnumActionResult.FAIL;
    }
}
