package me.dmillerw.droids.api.action;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.function.Supplier;

public class ActionFactory {

    private static Map<String, Supplier<BaseAction>> actionFactoryMap = Maps.newHashMap();

    static {
        actionFactoryMap.put(ActionPickupItem.KEY, ActionPickupItem::new);
    }

    public static BaseAction newInstance(String key) {
        return actionFactoryMap.get(key).get();
    }
}
