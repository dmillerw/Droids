package me.dmillerw.droids.common.item;

import me.dmillerw.droids.common.job.JobDefinition;
import me.dmillerw.droids.common.job.JobRegistry;
import me.dmillerw.droids.common.job.parameter.ParameterMap;
import me.dmillerw.droids.common.network.GuiHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemJobCard extends BaseItem {

    public static final String KEY = "job_card";

    public static ItemStack getBlankCard() {
        return new ItemStack(ModItems.job_card, 1, 0);
    }

    public static ItemStack getJobCardForJob(JobDefinition job) {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setString("jobKey", job.key);

        NBTTagCompound params = new NBTTagCompound();
        job.getBlankParameterMap().writeToNbt(params);
        tag.setTag("params", params);

        ItemStack itemStack = new ItemStack(ModItems.job_card, 1, 0);
        itemStack.setTagCompound(tag);

        return itemStack;
    }

    public static JobDefinition getJobFromCard(ItemStack itemStack) {
        if (itemStack.isEmpty() || itemStack.getTagCompound() == null)
            return null;

        NBTTagCompound tag = itemStack.getTagCompound();
        if (tag == null)
            return null;

        return JobRegistry.INSTANCE.getJob(tag.getString("jobKey"));
    }

    public static ParameterMap getParametersFromCard(JobDefinition job, ItemStack itemStack) {
        if (itemStack.isEmpty() || itemStack.getTagCompound() == null)
            return job.getBlankParameterMap();

        NBTTagCompound tag = itemStack.getTagCompound();
        if (tag == null)
            return job.getBlankParameterMap();

        ParameterMap map = new ParameterMap(job);
        map.readFromNbt(tag.getCompoundTag("params"));

        return map;
    }

    public static void updateParameters(ItemStack itemStack, ParameterMap parameters) {
        if (itemStack.isEmpty() || parameters == null)
            return;

        NBTTagCompound tag = itemStack.getTagCompound();
        if (tag == null) tag = new NBTTagCompound();

        NBTTagCompound params = new NBTTagCompound();
        parameters.writeToNbt(params);
        tag.setTag("params", params);

        itemStack.setTagCompound(tag);
    }

    public ItemJobCard() {
        super(KEY);

        setHasSubtypes(true);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);

        if (worldIn.isRemote && playerIn.isSneaking()) {
            if (ItemJobCard.getJobFromCard(stack) == null) {
                GuiHandler.Gui.SET_JOB.openGui(playerIn);
            } else {
                GuiHandler.Gui.CONFIGURE_JOB.openGui(playerIn);
            }
        }

        return ActionResult.newResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        JobDefinition job = ItemJobCard.getJobFromCard(stack);
        if (job == null) {
            tooltip.add("ERROR: CARD HAS NO JOB!");
            return;
        }

        tooltip.add(job.key);
        tooltip.add(job.description);

        tooltip.add(stack.getTagCompound() == null ? "" : stack.getTagCompound().toString());
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            items.add(ItemJobCard.getBlankCard());
            for (JobDefinition definition : JobRegistry.INSTANCE.getJobs()) {
                items.add(ItemJobCard.getJobCardForJob(definition));
            }
        }
    }
}
