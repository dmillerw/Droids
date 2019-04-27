package me.dmillerw.droids.common.network.packets;

import me.dmillerw.droids.common.network.PacketHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class BasePacket implements IMessage {

    public final void sendToServer() {
        PacketHandler.INSTANCE.sendToServer(this);
    }

    public final void sendToPlayer(EntityPlayerMP player) {
        PacketHandler.INSTANCE.sendTo(this, player);
    }

    public final void sendToWorld(World world) {
        PacketHandler.INSTANCE.sendToDimension(this, world.provider.getDimension());
    }

    public final void sendToAll() {
        PacketHandler.INSTANCE.sendToAll(this);
    }
}
