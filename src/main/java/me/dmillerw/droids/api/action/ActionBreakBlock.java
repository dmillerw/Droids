package me.dmillerw.droids.api.action;

import me.dmillerw.droids.api.IAIController;
import me.dmillerw.droids.api.IDroid;
import me.dmillerw.droids.common.entity.EntityDroid;
import net.minecraft.util.math.BlockPos;

public class ActionBreakBlock extends BaseAction {

    public static final String KEY = "break_block";

    private BlockPos targetPosition;

    public ActionBreakBlock() {
        super(KEY);
    }

    public ActionBreakBlock(BlockPos targetPosition) {
        super(KEY);

        this.targetPosition = targetPosition;
    }

    @Override
    public int getDistanceToDroid(IDroid droid) {
        return (int) targetPosition.distanceSq(((EntityDroid)droid).getPosition());
    }

    @Override
    public void tick(IAIController controller, IDroid droid) {
        if (droid.getAiHelper().getToBlock(targetPosition)) {
            controller.getTileWorld().setBlockToAir(targetPosition);
            markAsComplete();
        }
    }
}
