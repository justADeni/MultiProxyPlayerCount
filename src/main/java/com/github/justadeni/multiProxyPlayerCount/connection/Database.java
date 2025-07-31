package com.github.justadeni.multiProxyPlayerCount.connection;

import com.velocitypowered.api.proxy.Player;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface Database {

    public void add(Player player);

    public void remove(Player player);

    public CompletableFuture<Set<PlayerData>> query();

}
