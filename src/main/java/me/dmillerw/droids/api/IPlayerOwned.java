package me.dmillerw.droids.api;

import java.util.UUID;

public interface IPlayerOwned {

    public UUID getOwner();
    public void setOwner(UUID owner);

    public boolean canConnect(IPlayerOwned other);
}
