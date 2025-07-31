package com.github.justadeni.multiProxyPlayerCount.listeners;

import com.github.justadeni.multiProxyPlayerCount.connection.Database;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;

public class PlayerLeaveListener {

    private final Database database;

    public PlayerLeaveListener(Database database) {
        this.database = database;
    }

    @Subscribe
    public void onPlayerLeave(DisconnectEvent event) {
        database.remove(event.getPlayer());
    }

}
