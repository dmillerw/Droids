package me.dmillerw.droids.client.event;

import me.dmillerw.droids.common.block.ModBlocks;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by dmillerw
 */
@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientRegistryHandler {

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent evt) {
//        registerItemBlockModel(ModBlocks.remote_interface_item, "inventory");
//        registerItemBlockModel(ModBlocks.analyzer_item, "inventory");

//        registerItemModel(ModItems.debug);
//        registerItemModel(ModItems.workbook);
//        registerItemModel(ModItems.atlas);

        registerItemBlockModel(ModBlocks.ai_controller_item, "inventory");
        registerItemBlockModel(ModBlocks.charging_station_item, "inventory");
        registerItemBlockModel(ModBlocks.fluid_router_item, "inventory");
        registerItemBlockModel(ModBlocks.item_router_item, "inventory");
        registerItemBlockModel(ModBlocks.power_router_item, "inventory");
    }

    private static void registerItemModel(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName().toString()));
    }

    private static void registerItemBlockModel(Item item, String tag) {
        ModelResourceLocation resourceLocation = new ModelResourceLocation(item.getRegistryName(), tag);
        ModelLoader.setCustomModelResourceLocation(item, 0, resourceLocation);
    }
}