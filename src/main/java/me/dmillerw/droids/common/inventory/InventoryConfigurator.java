package me.dmillerw.droids.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;

public class InventoryConfigurator implements IInventory {

    private static final int SIZE = 1;

    private ItemStack item;
    private NonNullList<ItemStack> inventory = NonNullList.withSize(SIZE, ItemStack.EMPTY);

    public InventoryConfigurator(ItemStack item) {
        this.item = item;

        loadInventory();
    }

    private void loadInventory() {
        NBTTagCompound tag = item.getTagCompound();
        if (tag == null)
            return;

        ItemStackHelper.loadAllItems(tag, inventory);
    }

    private void saveInventory() {
        NBTTagCompound tag = item.getTagCompound();
        if (tag == null) tag = new NBTTagCompound();

        ItemStackHelper.saveAllItems(tag, inventory);

        item.setTagCompound(tag);
    }

    @Override
    public int getSizeInventory() {
        return SIZE;
    }

    @Override
    public boolean isEmpty() {
        return inventory.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return inventory.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack item = getStackInSlot(index);
        if (item.isEmpty())
            return ItemStack.EMPTY;

        ItemStack split = item.splitStack(count);

        saveInventory();

        return split;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack item = inventory.get(index).copy();
        setInventorySlotContents(index, ItemStack.EMPTY);
        return item;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        inventory.set(index, stack);

        saveInventory();
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return false;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        inventory = NonNullList.withSize(SIZE, ItemStack.EMPTY);
        saveInventory();
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return null;
    }
}
