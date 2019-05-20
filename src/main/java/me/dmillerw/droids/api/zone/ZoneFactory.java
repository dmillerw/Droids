package me.dmillerw.droids.api.zone;

import com.google.common.collect.Maps;
import me.dmillerw.droids.api.action.ActionPickupItem;

import java.util.Map;
import java.util.function.Supplier;

public class ZoneFactory {

    private static Map<String, Supplier<BaseZone>> zoneFactoryMap = Maps.newHashMap();

    static {
        zoneFactoryMap.put(ZoneStockpile.KEY, ZoneStockpile::new);
    }

    public static BaseZone newInstance(String key) {
        return zoneFactoryMap.get(key).get();
    }
}
