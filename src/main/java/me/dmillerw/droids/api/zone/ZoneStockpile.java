package me.dmillerw.droids.api.zone;

public class ZoneStockpile extends BaseZone {

    public static final String KEY = "stockpile";

    public ZoneStockpile(String zoneType) {
        super(zoneType);
    }

    public ZoneStockpile() {
        super(KEY);
    }
}
