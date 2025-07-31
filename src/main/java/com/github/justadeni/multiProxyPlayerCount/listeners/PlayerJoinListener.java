package com.github.justadeni.multiProxyPlayerCount.listeners;

import com.github.justadeni.multiProxyPlayerCount.connection.Database;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;

public class PlayerJoinListener {

    private final Database database;

    public PlayerJoinListener(Database database) {
        this.database = database;
    }

    @Subscribe
    public void onPlayerJoin(PostLoginEvent event) {
        database.add(event.getPlayer());
    }

}
