package com.github.justadeni.multiProxyPlayerCount;

import com.github.justadeni.multiProxyPlayerCount.listeners.PlayerJoinListener;
import com.github.justadeni.multiProxyPlayerCount.listeners.PlayerLeaveListener;
import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

@Plugin(
    id = "multiproxyplayercount",
    name = "MultiProxyPlayerCount",
    version = "0.0.1"
    ,description = "Plugin that allows multiple proxy networks to share a player list between them."
    ,url = "https://github.com/justADeni/MultiProxyPlayerCount"
    ,authors = {"justADeni"}
)
public class MultiProxyPlayerCount {

    private final ProxyServer server;
    private final Logger logger;

    @Inject
    public MultiProxyPlayerCount(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    @Subscribe
    public void onInitialize(ProxyInitializeEvent event) {
        server.getEventManager().register(this, new PlayerJoinListener());
        server.getEventManager().register(this, new PlayerLeaveListener());
    }
}
