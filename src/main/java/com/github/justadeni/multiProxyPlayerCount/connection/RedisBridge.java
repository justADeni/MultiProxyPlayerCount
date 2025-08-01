package com.github.justadeni.multiProxyPlayerCount.connection;

import com.velocitypowered.api.proxy.Player;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class RedisBridge implements Database {

    private static final ExecutorService VIRTUAL_EXECUTOR = Executors.newVirtualThreadPerTaskExecutor();
    private static final String REDIS_SET_KEY = "multiproxyplayercount:online-players";
    private static final JedisPool pool = new JedisPool("localhost", 6379);

    @Override
    public void add(Player player) {
        try (Jedis jedis = pool.getResource()) {
            jedis.hset(REDIS_SET_KEY, player.getUniqueId().toString(), player.getUsername());
        }
    }

    @Override
    public void remove(Player player) {
        try (Jedis jedis = pool.getResource()) {
            jedis.hdel(REDIS_SET_KEY, player.getUniqueId().toString());
        }
    }

    @Override
    public CompletableFuture<Set<PlayerData>> query() {
        return CompletableFuture.supplyAsync(() -> {
            try (Jedis jedis = pool.getResource()) {
                Map<String, String> entries = jedis.hgetAll(REDIS_SET_KEY);
                return entries.entrySet().stream()
                        .map(entry -> {
                            UUID uuid = UUID.fromString(entry.getKey());
                            String[] key = entry.getValue().split(",", 1);
                            return new PlayerData(uuid, key[0], key[1]);
                        })
                        .collect(Collectors.toSet());
            }
        }, VIRTUAL_EXECUTOR);
    }
}
