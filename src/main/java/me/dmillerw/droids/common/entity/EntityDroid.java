package me.dmillerw.droids.common.entity;

import com.google.common.base.Optional;
import me.dmillerw.droids.api.IDroid;
import me.dmillerw.droids.common.entity.ai.AIHelper;
import me.dmillerw.droids.common.entity.ai.EntityAIExecuteTask;
import me.dmillerw.droids.common.helper.DroidTracker;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.UUID;

public class EntityDroid extends EntityLiving implements IDroid {

    public static final int TOOLBELT_SLOTS = 4;
    public static final int INVENTORY_SLOTS = 9;

    private static final DataParameter<Optional<UUID>> OWNER = EntityDataManager.createKey(EntityDroid.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    private static final DataParameter<String> SKIN = EntityDataManager.createKey(EntityDroid.class, DataSerializers.STRING);
    private static final DataParameter<ItemStack> HELD_ITEM = EntityDataManager.createKey(EntityDroid.class, DataSerializers.ITEM_STACK);

    private UUID owner;
    public AIHelper aiHelper;

    private NonNullList<ItemStack> toolbelt = NonNullList.withSize(4, ItemStack.EMPTY);
    private NonNullList<ItemStack> inventory = NonNullList.withSize(INVENTORY_SLOTS, ItemStack.EMPTY);

    private int runningAction = -1;

    public EntityDroid(World world) {
        super(world);

        enablePersistence();

        this.aiHelper = new AIHelper(this, 0.45F);
    }

    public EntityDroid(World worldIn, EntityPlayer owner) {
        super(worldIn);

        enablePersistence();

        this.owner = owner.getGameProfile().getId();
        this.aiHelper = new AIHelper(this, 0.45F);
    }

    @Override
    protected void entityInit() {
        super.entityInit();

        dataManager.register(OWNER, owner == null ? Optional.absent() : Optional.of(owner));
        dataManager.register(SKIN, "");
        dataManager.register(HELD_ITEM, ItemStack.EMPTY);

        if (!world.isRemote) {
            DroidTracker.startTrackingDroid(this);
        }
    }

    @Override
    public void setDead() {
        super.setDead();

        if (!world.isRemote) {
            DroidTracker.stopTrackingDroid(this);
        }
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();

        this.tasks.addTask(1, new EntityAIExecuteTask(this, 0.45));
    }

    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand) {
        Optional<UUID> owner = getDataManager().get(OWNER);
        if (!owner.isPresent() || !player.getGameProfile().getId().equals(owner.get()))
            return false;

        if (player.getHeldItem(hand).isEmpty()) {
            if (player.world.isRemote) {
//                Minecraft.getMinecraft().displayGuiScreen(new GuiMinion(this));
            }
        }

        return player.getHeldItem(hand).isEmpty();
    }

    /* GET SET */

    public UUID getOwner() {
        return dataManager.get(OWNER).orNull();
    }

    public int getAction() {
        return runningAction;
    }

    public void setAction(int action) {
        this.runningAction = action;
    }

    /* OVERRIDES */

    @Override
    public void setCustomNameTag(String name) {
        setSkin(name);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);

        compound.setString("skin", getSkin());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);

        if (compound.hasKey("skin"))
            setSkin(compound.getString("skin"));
        else
            setSkin("");
    }

    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {
        return slotIn == EntityEquipmentSlot.MAINHAND ? dataManager.get(HELD_ITEM) : ItemStack.EMPTY;
    }

    @Override
    public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {
        if (slotIn == EntityEquipmentSlot.MAINHAND)
            dataManager.set(HELD_ITEM, stack.copy());
    }

    @Override
    public EnumHandSide getPrimaryHand() {
        return EnumHandSide.RIGHT;
    }

    /* GETTERS / SETTERS */

    @Override
    public World getWorld() {
        return world;
    }

    public ItemStack getHeldItem() {
        return getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
    }

    @Override
    public AIHelper getAiHelper() {
        return aiHelper;
    }

    public String getSkin() {
        return dataManager.get(SKIN);
    }

    public void setSkin(String skin) {
        dataManager.set(SKIN, skin);
    }
}
