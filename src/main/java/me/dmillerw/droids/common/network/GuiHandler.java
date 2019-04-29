package me.dmillerw.droids.common.network;

import me.dmillerw.droids.Droids;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class GuiHandler implements IGuiHandler {

    public static enum Gui {

        SET_JOB,
        CONFIGURE_JOB;

        public void openGui(EntityPlayer player) {
            player.openGui(Droids.INSTANCE, ordinal(), player.world, 0, 0 ,0);
        }

        public void openGui(EntityPlayer player, int x, int y, int z) {
            player.openGui(Droids.INSTANCE, ordinal(), player.world, x, y, z);
        }
    }

    @Nullable
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        Gui gui = Gui.values()[id];
        switch (gui) {
            case SET_JOB:
                return null;
            case CONFIGURE_JOB:
                return null;
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        Gui gui = Gui.values()[id];
//        switch (gui) {
//            case SET_JOB:
//                return new GuiSetJob(player.getHeldItem(EnumHand.MAIN_HAND));
//            case CONFIGURE_JOB:
//                return new GuiModifyJobCard(player, player.getHeldItem(EnumHand.MAIN_HAND));
//        }
        return null;
    }
}
