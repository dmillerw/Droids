package me.dmillerw.droids.common.event;

import me.dmillerw.droids.common.helper.DroidTracker;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WorldEventHandler {

    @SubscribeEvent
    public static void onWorldUnload(WorldEvent.Unload event) {
        DroidTracker.clearDimension(event.getWorld().provider.getDimension());
    }
}
