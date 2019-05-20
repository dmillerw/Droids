package me.dmillerw.droids.proxy;

import me.dmillerw.droids.Droids;
import me.dmillerw.droids.common.ModInfo;
import me.dmillerw.droids.common.handler.ActionHandler;
import me.dmillerw.droids.common.block.*;
import me.dmillerw.droids.common.entity.EntityDroid;
import me.dmillerw.droids.common.event.WorldEventHandler;
import me.dmillerw.droids.common.helper.DroidTracker;
import me.dmillerw.droids.common.job.JobRegistry;
import me.dmillerw.droids.common.job.impl.JobCollectItems;
import me.dmillerw.droids.common.job.impl.JobHarvestPlants;
import me.dmillerw.droids.common.job.impl.JobHarvestTrees;
import me.dmillerw.droids.common.network.GuiHandler;
import me.dmillerw.droids.common.network.PacketHandler;
import me.dmillerw.droids.common.tile.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy implements IProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        PacketHandler.initialize();
        NetworkRegistry.INSTANCE.registerGuiHandler(Droids.INSTANCE, new GuiHandler());

        MinecraftForge.EVENT_BUS.register(WorldEventHandler.class);
        MinecraftForge.EVENT_BUS.register(DroidTracker.class);
        MinecraftForge.EVENT_BUS.register(ActionHandler.class);

        GameRegistry.registerTileEntity(TileAIController.class, new ResourceLocation(ModInfo.ID, BlockAIController.NAME));
        GameRegistry.registerTileEntity(TileChargingStation.class, new ResourceLocation(ModInfo.ID, BlockChargingStation.NAME));
        GameRegistry.registerTileEntity(TileFluidRouter.class, new ResourceLocation(ModInfo.ID, BlockFluidRouter.NAME));
        GameRegistry.registerTileEntity(TileItemRouter.class, new ResourceLocation(ModInfo.ID, BlockItemRouter.NAME));
        GameRegistry.registerTileEntity(TilePowerRouter.class, new ResourceLocation(ModInfo.ID, BlockPowerRouter.NAME));

        EntityRegistry.registerModEntity(
                new ResourceLocation(ModInfo.ID, "droid"),
                EntityDroid.class,
                "droid",
                1,
                Droids.INSTANCE,
                64,
                3,
                true,
                0,
                1);

        JobRegistry.INSTANCE.registerJobType(new JobHarvestPlants());
        JobRegistry.INSTANCE.registerJobType(new JobHarvestTrees());
        JobRegistry.INSTANCE.registerJobType(new JobCollectItems());
    }

    @Override
    public void init(FMLInitializationEvent event) {

    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {

    }
}
