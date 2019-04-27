package me.dmillerw.droids.client.network;

import com.google.common.collect.Sets;
import me.dmillerw.droids.client.network.packets.CClaimDebug;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

public class ClientSyncHandler {

    public static final ClientSyncHandler INSTANCE = new ClientSyncHandler();

    private Set<BlockPos> debugClaimedBlocks = Sets.newHashSet();
    private Set<UUID> debugClaimedEntities = Sets.newHashSet();

    public void handleClaimDebug(CClaimDebug message) {
        if (message.type == CClaimDebug.CLAIM) {
            if (message.targetBlock != null) {
                debugClaimedBlocks.add(message.targetBlock);
            } else if (message.targetEntity != null) {
                debugClaimedEntities.add(message.targetEntity);
            }
        } else if (message.type == CClaimDebug.RELEASE) {
            if (message.targetBlock != null) {
                debugClaimedBlocks.remove(message.targetBlock);
            } else if (message.targetEntity != null) {
                debugClaimedEntities.remove(message.targetEntity);
            }
        }
    }

    public CopyOnWriteArraySet<BlockPos> getClaimedBlocks() {
        return new CopyOnWriteArraySet<>(debugClaimedBlocks);
    }

    public CopyOnWriteArraySet<UUID> getClaimedEntities() {
        return new CopyOnWriteArraySet<>(debugClaimedEntities);
    }

    public boolean isClaimed(BlockPos pos) {
        return debugClaimedBlocks.contains(pos);
    }

    public boolean isClaimed(Entity entity) {
        return debugClaimedEntities.contains(entity.getPersistentID());
    }
}
