package com.github.justadeni.multiProxyPlayerCount.connection;

import java.util.UUID;

public record PlayerData(UUID uuid, String playerName, String proxyIdentifier){}