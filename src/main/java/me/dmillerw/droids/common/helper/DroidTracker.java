package me.dmillerw.droids.common.helper;

import com.google.common.collect.Maps;
import me.dmillerw.droids.api.IAIController;
import me.dmillerw.droids.common.entity.EntityDroid;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class DroidTracker {

    private static Map<Integer, Map<UUID, EntityDroid>> droidTracker = Maps.newHashMap();

    public static void clearDimension(int dimension) {
        droidTracker.remove(dimension);
    }

    public static void startTrackingDroid(EntityDroid droid) {
        Map<UUID, EntityDroid> map = droidTracker.get(droid.world.provider.getDimension());
        if (map == null) map = Maps.newHashMap();

        map.put(droid.getPersistentID(), droid);

        droidTracker.put(droid.world.provider.getDimension(), map);
    }

    public static EntityDroid getDroid(World world, UUID droidId) {
        Map<UUID, EntityDroid> map = droidTracker.get(world.provider.getDimension());
        if (map == null)
            return null;

        return map.get(droidId);
    }

    public static void stopTrackingDroid(EntityDroid droid) {
        Map<UUID, EntityDroid> map = droidTracker.get(droid.world.provider.getDimension());
        if (map == null) return;

        map.remove(droid.getPersistentID());

        droidTracker.put(droid.world.provider.getDimension(), map);
    }

    public static List<EntityDroid> getAvailableDroids(World world, IAIController controller) {
        Map<UUID, EntityDroid> map = droidTracker.get(world.provider.getDimension());
        if (map == null) return Collections.EMPTY_LIST;
        return map.values().stream()
//                .filter((droid) -> network.isPositionInNetwork(droid.getPosition()))
                .filter((droid) -> droid.getAction() == -1)
                .collect(Collectors.toList());
    }
}
