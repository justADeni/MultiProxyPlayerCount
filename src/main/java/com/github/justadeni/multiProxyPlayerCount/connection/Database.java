package com.github.justadeni.multiProxyPlayerCount.connection;

import com.velocitypowered.api.proxy.Player;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface Database {

    void add(Player player);

    void remove(Player player);

    CompletableFuture<Set<PlayerData>> query();

}
