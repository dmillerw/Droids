package me.dmillerw.droids.common.network.packets;

import io.netty.buffer.ByteBuf;
import me.dmillerw.droids.common.item.ItemJobCard;
import me.dmillerw.droids.common.item.ModItems;
import me.dmillerw.droids.common.job.JobDefinition;
import me.dmillerw.droids.common.job.JobRegistry;
import me.dmillerw.droids.common.job.parameter.ParameterMap;
import me.dmillerw.droids.common.network.PacketHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SUpdateJobCard extends BasePacket {

    public String jobId;
    public ParameterMap parameterMap;

    @Override
    public void fromBytes(ByteBuf buf) {
        jobId = ByteBufUtils.readUTF8String(buf);

        if (buf.readBoolean())
            parameterMap = ParameterMap.fromNbt(ByteBufUtils.readTag(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, jobId);

        if (parameterMap == null) {
            buf.writeBoolean(false);
        } else {
            buf.writeBoolean(true);

            NBTTagCompound tag = new NBTTagCompound();
            parameterMap.writeToNbt(tag);

            ByteBufUtils.writeTag(buf, tag);
        }
    }

    public static class Handler implements IMessageHandler<SUpdateJobCard, IMessage> {

        @Override
        public IMessage onMessage(SUpdateJobCard message, MessageContext ctx) {
            PacketHandler.addScheduledTask(ctx.netHandler, () -> {
                EntityPlayerMP mp = ctx.getServerHandler().player;
                ItemStack heldItem = mp.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);

                if (heldItem.getItem() != ModItems.job_card)
                    return;

                if (message.jobId != null) {
                    JobDefinition job = JobRegistry.INSTANCE.getJob(message.jobId);
                    if (job == null)
                        return;

                    heldItem = ItemJobCard.getJobCardForJob(job);

                    if (message.parameterMap == null)
                        ItemJobCard.updateParameters(heldItem, job.getBlankParameterMap());
                }

                if (message.parameterMap != null) {
                    ItemJobCard.updateParameters(heldItem, message.parameterMap);
                }

                mp.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, heldItem.copy());
                mp.connection.sendPacket(new SPacketSetSlot(-2, mp.inventory.currentItem, heldItem.copy()));
            });
            return null;
        }
    }
}
