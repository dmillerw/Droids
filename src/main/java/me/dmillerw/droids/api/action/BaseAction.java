package me.dmillerw.droids.api.action;

import me.dmillerw.droids.api.IAIController;
import me.dmillerw.droids.api.IDroid;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public abstract class BaseAction {

    public static enum Progress {

        INIITIAL, IN_PROGRESS, COMPLETE, CANCELLED
    }

    public static enum CancelReason {

        DEAD,
        CANT_PATH
    }

    public static BaseAction fromNbt(NBTTagCompound tag) {
        String key = tag.getString("key");
        BaseAction action = ActionFactory.newInstance(key);
        action.readFromNbt(tag);
        return action;
    }

    public final String key;
    private int actionId;

    private Progress progress = Progress.INIITIAL;
    private CancelReason cancelReason = null;

    private BlockPos providerOrigin;
    private UUID droidId;

    public BaseAction(String key) {
        this.key = key;
    }

    public final int getActionId() {
        return actionId;
    }

    public final void setActionId(int actionId) {
        this.actionId = actionId;
    }

    public final Progress getProgress() {
        return progress;
    }

    public final CancelReason getCancelReason() {
        return cancelReason;
    }

    public final void markAsInProgress() {
        this.progress = Progress.IN_PROGRESS;
    }

    public final void markAsCancelled(CancelReason cancelReason) {
        this.progress = Progress.CANCELLED;
        this.cancelReason = cancelReason;
    }

    public final void markAsComplete() {
        this.progress = Progress.COMPLETE;
    }

    public final UUID getDroidId() {
        return droidId;
    }

    public final void setDroidId(UUID uuid) {
        this.droidId = uuid;
    }

    public BlockPos getProviderOrigin() {
        return providerOrigin;
    }

    public final void setProviderOrigin(BlockPos providerOrigin) {
        this.providerOrigin = providerOrigin;
    }

    public boolean canRunWithOtherActions() {
        return false;
    }

    public void writeToNbt(NBTTagCompound tag) {
        tag.setString("key", key);

        tag.setInteger("progress", progress.ordinal());
        if (cancelReason != null)
            tag.setInteger("cancel_reason", cancelReason.ordinal());

        if (providerOrigin != null)
            tag.setLong("provider_origin", providerOrigin.toLong());

        if (droidId != null) {
            tag.setUniqueId("droid_id", droidId);
        }
    }

    public void readFromNbt(NBTTagCompound tag) {
        progress = Progress.values()[tag.getInteger("progress")];

        if (tag.hasKey("cancel_reason"))
            cancelReason = CancelReason.values()[tag.getInteger("cancel_reason")];

        if (tag.hasKey("provider_origin"))
            providerOrigin = BlockPos.fromLong(tag.getLong("provider_origin"));

        if (tag.hasKey("droid_id"))
            droidId = tag.getUniqueId("droid_id");
    }

    public abstract int getDistanceToDroid(IDroid droid);

    public abstract void tick(IAIController controller, IDroid droid);
}
