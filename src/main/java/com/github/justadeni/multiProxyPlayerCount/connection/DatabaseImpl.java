package com.github.justadeni.multiProxyPlayerCount.connection;

import com.github.justadeni.multiProxyPlayerCount.config.Config;
import com.velocitypowered.api.proxy.Player;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class DatabaseImpl implements Database {

    private static final ExecutorService VIRTUAL_EXECUTOR = Executors.newVirtualThreadPerTaskExecutor();
    private static final String REDIS_SET_KEY = "multiproxyplayercount:online-players";

    private final Config config;
    private final JedisPool pool;

    public DatabaseImpl(Config config) {
        this.config = config;
        pool = new JedisPool(
                config.getHost(),
                config.getPort(),
                config.getUser(),
                config.getPassword()
        );
    }

    @Override
    public CompletableFuture<Boolean> connectionTest() {
        return CompletableFuture.supplyAsync(() -> {
            try (Jedis jedis = pool.getResource()) {
                return "PONG".equals(jedis.ping());
            } catch (Exception e) {
                return false;
            }
        }, VIRTUAL_EXECUTOR);
    }

    @Override
    public void add(Player player) {
        VIRTUAL_EXECUTOR.submit(() -> {
            try (Jedis jedis = pool.getResource()) {
                jedis.hset(REDIS_SET_KEY, player.getUniqueId().toString(), player.getUsername() + "," + config.getProxyIdentifier());
            }
        });
    }

    @Override
    public void remove(Player player) {
        VIRTUAL_EXECUTOR.submit(() -> {
            try (Jedis jedis = pool.getResource()) {
                jedis.hdel(REDIS_SET_KEY, player.getUniqueId().toString());
            }
        });
    }

    @Override
    public CompletableFuture<Set<PlayerData>> query() {
        return CompletableFuture.supplyAsync(() -> {
            try (Jedis jedis = pool.getResource()) {
                Map<String, String> entries = jedis.hgetAll(REDIS_SET_KEY);
                return entries.entrySet().stream()
                        .map(entry -> {
                            UUID uuid = UUID.fromString(entry.getKey());
                            String[] key = entry.getValue().split(",", 2);
                            return new PlayerData(uuid, key[0], key[1]);
                        })
                        .collect(Collectors.toSet());
            }
        }, VIRTUAL_EXECUTOR);
    }
}
