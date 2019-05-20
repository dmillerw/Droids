package me.dmillerw.droids.common.entity.ai;

import me.dmillerw.droids.api.IAIController;
import me.dmillerw.droids.api.action.BaseAction;
import me.dmillerw.droids.common.handler.ActionHandler;
import me.dmillerw.droids.common.entity.EntityDroid;
import me.dmillerw.droids.common.item.ItemConfigurator;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.tileentity.TileEntity;

public class EntityAIExecuteTask extends EntityAIBase {

    private final EntityDroid droid;
    public final double speed;

    public EntityAIExecuteTask(EntityDroid droid, double speed) {
        this.droid = droid;
        this.speed = speed;

        this.setMutexBits(7);
    }

    @Override
    public boolean shouldExecute() {
        return droid.getAction() != -1;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return shouldExecute();
    }

    @Override
    public void updateTask() {
        if (droid.world.isRemote)
            return;

        if (ItemConfigurator.debugPauseDroidTicks)
            return;

        ActionHandler handler = ActionHandler.get(droid.world);
        int actionId = droid.getAction();

        if (actionId == -1)
            return;

        BaseAction action = handler.getAction(actionId);
        if (action == null)
            return;

        TileEntity tile = droid.world.getTileEntity(action.getProviderOrigin());
        if (tile == null)
            return;

        action.tick((IAIController) tile, droid);

        if (action.getProgress() == BaseAction.Progress.COMPLETE || action.getProgress() == BaseAction.Progress.CANCELLED)
            droid.setAction(-1);

        handler.updateAction(action);
    }
}
