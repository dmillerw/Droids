package me.dmillerw.droids.api.action;

import me.dmillerw.droids.api.IAIController;
import me.dmillerw.droids.api.IDroid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.nbt.NBTTagCompound;

import java.util.UUID;

public class ActionPickupItem extends BaseAction {

    public static final String KEY = "pickup_item";

    private UUID entityId;

    public ActionPickupItem() {
        super(KEY);
    }

    public ActionPickupItem(EntityItem item) {
        super(KEY);

        this.entityId = item.getPersistentID();
    }

    @Override
    public void writeToNbt(NBTTagCompound tag) {
        super.writeToNbt(tag);

        tag.setUniqueId("entity_id", entityId);
    }

    @Override
    public void readFromNbt(NBTTagCompound tag) {
        super.readFromNbt(tag);

        entityId = tag.getUniqueId("entity_id");
    }

    @Override
    public int getDistanceToDroid(IDroid droid) {
        Entity entity = droid.getWorld().getLoadedEntityList().stream()
                .filter((e) -> e.getUniqueID().equals(entityId))
                .findAny().orElse(null);

        if (entity == null) {
            markAsCancelled(CancelReason.DEAD);
            return Integer.MAX_VALUE;
        }

        return (int) entity.getDistanceSq((Entity)droid);
    }

    @Override
    public void tick(IAIController controller, IDroid droid) {
        EntityItem target = droid.getAiHelper().getEntityForId(entityId);
        if (target == null || target.isDead)
            markAsCancelled(CancelReason.DEAD);

        if (droid.getAiHelper().pickupItem(target))
            markAsComplete();
        else
            markAsCancelled(CancelReason.CANT_PATH);
    }
}
