package me.dmillerw.droids.common.network;

import me.dmillerw.droids.client.network.packets.CClaimDebug;
import me.dmillerw.droids.common.ModInfo;
import me.dmillerw.droids.common.network.packets.SUpdateJobCard;
import net.minecraft.network.INetHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(ModInfo.ID);

    public static void addScheduledTask(INetHandler netHandler, Runnable runnable) {
        FMLCommonHandler.instance().getWorldThread(netHandler).addScheduledTask(runnable);
    }

    private static int clientDiscriminator = 1;
    private static int serverDiscriminator = -1;

    private static <REQ extends IMessage, REPLY extends IMessage> void client(Class<REQ> requestMessageType, Class<? extends IMessageHandler<REQ, REPLY>> messageHandler) {
        INSTANCE.registerMessage(messageHandler, requestMessageType, clientDiscriminator, Side.CLIENT);
        clientDiscriminator++;
    }

    private static <REQ extends IMessage, REPLY extends IMessage> void server(Class<REQ> requestMessageType, Class<? extends IMessageHandler<REQ, REPLY>> messageHandler) {
        INSTANCE.registerMessage(messageHandler, requestMessageType, serverDiscriminator, Side.SERVER);
        serverDiscriminator--;
    }

    public static void initialize() {
        client(CClaimDebug.class, CClaimDebug.Handler.class);

        server(SUpdateJobCard.class, SUpdateJobCard.Handler.class);
    }
}
