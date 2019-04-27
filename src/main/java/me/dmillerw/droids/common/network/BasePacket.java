package me.dmillerw.droids.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;

public abstract class BasePacket implements IMessage {

    public abstract Class<? extends IMessageHandler<? extends BasePacket, ?>> getPacketHandler();

    public abstract void toBytes(ByteBuf buf);
    public abstract void fromBytes(ByteBuf buf);

    public final void sendToServer() {
        PacketHandler.INSTANCE.sendToServer(this);
    }
}
