package me.dmillerw.droids.client.network.packets;

import io.netty.buffer.ByteBuf;
import me.dmillerw.droids.client.network.ClientSyncHandler;
import me.dmillerw.droids.common.network.packets.BasePacket;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class CClaimDebug extends BasePacket {

    public static final int CLAIM = 0;
    public static final int RELEASE = 1;

    public int type;
    public BlockPos targetBlock;
    public UUID targetEntity;

    public CClaimDebug() {}

    public CClaimDebug(int type, BlockPos targetBlock) {
        this.type = type;
        this.targetBlock = targetBlock;
    }

    public CClaimDebug(int type, UUID targetEntity) {
        this.type = type;
        this.targetEntity = targetEntity;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(type);

        if (targetBlock == null) {
            buf.writeBoolean(false);
        } else {
            buf.writeBoolean(true);
            buf.writeLong(targetBlock.toLong());
        }

        if (targetEntity == null) {
            buf.writeBoolean(false);
        } else {
            buf.writeBoolean(true);
            buf.writeLong(targetEntity.getMostSignificantBits());
            buf.writeLong(targetEntity.getLeastSignificantBits());
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        type = buf.readInt();

        if (buf.readBoolean()) {
            targetBlock = BlockPos.fromLong(buf.readLong());
        }

        if (buf.readBoolean()) {
            targetEntity = new UUID(buf.readLong(), buf.readLong());
        }
    }

    public static class Handler implements IMessageHandler<CClaimDebug, IMessage> {

        @Override
        public IMessage onMessage(CClaimDebug message, MessageContext ctx) {
            ClientSyncHandler.INSTANCE.handleClaimDebug(message);
            return null;
        }
    }
}
