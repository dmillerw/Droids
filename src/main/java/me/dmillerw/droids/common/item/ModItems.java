package me.dmillerw.droids.common.item;

import me.dmillerw.droids.common.ModInfo;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(ModInfo.ID)
public class ModItems {

    public static final ItemConfigurator configurator = null;
    public static final Item job_card = null;

    @Mod.EventBusSubscriber
    public static class RegistrationHandler {

        @SubscribeEvent
        public static void addItems(RegistryEvent.Register<Item> event) {
            event.getRegistry().registerAll(
                    new ItemConfigurator(),
                    new ItemJobCard()
            );
        }
    }
}
