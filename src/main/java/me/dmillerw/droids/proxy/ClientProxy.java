package me.dmillerw.droids.proxy;

import me.dmillerw.droids.client.event.DebugBoundsRenderer;
import me.dmillerw.droids.client.render.RenderDroid;
import me.dmillerw.droids.common.entity.EntityDroid;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        MinecraftForge.EVENT_BUS.register(DebugBoundsRenderer.class);

        RenderingRegistry.registerEntityRenderingHandler(EntityDroid.class, RenderDroid::new);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }
}
