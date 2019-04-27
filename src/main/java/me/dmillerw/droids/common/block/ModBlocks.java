package me.dmillerw.droids.common.block;

import me.dmillerw.droids.common.ModInfo;
import me.dmillerw.droids.common.item.block.ItemBlockPlayerOwned;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(ModInfo.ID)
public class ModBlocks {

    public static final BlockAIController ai_controller = null;
    @GameRegistry.ObjectHolder(ModInfo.ID + ":ai_controller")
    public static ItemBlock ai_controller_item = null;

    public static final BlockChargingStation charging_station = null;
    @GameRegistry.ObjectHolder(ModInfo.ID + ":charging_station")
    public static ItemBlock charging_station_item = null;

    public static final BlockItemRouter item_router = null;
    @GameRegistry.ObjectHolder(ModInfo.ID + ":item_router")
    public static ItemBlock item_router_item = null;

    public static final BlockFluidRouter fluid_router = null;
    @GameRegistry.ObjectHolder(ModInfo.ID + ":fluid_router")
    public static ItemBlock fluid_router_item = null;

    public static final BlockPowerRouter power_router = null;
    @GameRegistry.ObjectHolder(ModInfo.ID + ":power_router")
    public static ItemBlock power_router_item = null;

    @Mod.EventBusSubscriber
    public static class RegistrationHandler {

        @SubscribeEvent
        public static void addBlocks(RegistryEvent.Register<Block> event) {
            event.getRegistry().registerAll(
                    new BlockAIController(),
                    new BlockChargingStation(),
                    new BlockItemRouter(),
                    new BlockFluidRouter(),
                    new BlockPowerRouter()
            );
        }

        @SubscribeEvent
        public static void addItems(RegistryEvent.Register<Item> event) {
            event.getRegistry().registerAll(
                    itemBlockOwned(ai_controller),
                    itemBlockOwned(charging_station),
                    itemBlockOwned(item_router),
                    itemBlockOwned(fluid_router),
                    itemBlockOwned(power_router)
            );
        }

        private static ItemBlock itemBlock(Block block) {
            return (ItemBlock) new ItemBlock(block).setRegistryName(block.getRegistryName());
        }

        private static ItemBlock itemBlockOwned(Block block) {
            return (ItemBlock) new ItemBlockPlayerOwned(block).setRegistryName(block.getRegistryName());
        }
    }
}
