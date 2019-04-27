package me.dmillerw.droids.api;

import me.dmillerw.droids.api.action.BaseAction;
import me.dmillerw.droids.common.entity.EntityDroid;

public abstract class Requirement {

    public abstract boolean canPerform(EntityDroid droid);

    public abstract BaseAction getSatisfyingAction();
}
