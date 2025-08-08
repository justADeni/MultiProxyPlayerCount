package com.github.justadeni.multiProxyPlayerCount;

import com.github.justadeni.multiProxyPlayerCount.command.PluginCommand;
import com.github.justadeni.multiProxyPlayerCount.config.Config;
import com.github.justadeni.multiProxyPlayerCount.config.ConfigImpl;
import com.github.justadeni.multiProxyPlayerCount.connection.Database;
import com.github.justadeni.multiProxyPlayerCount.connection.DatabaseImpl;
import com.github.justadeni.multiProxyPlayerCount.listeners.PlayerJoinListener;
import com.github.justadeni.multiProxyPlayerCount.listeners.PlayerLeaveListener;
import com.google.inject.Inject;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(
    id = "${project.artifactId}",
    name = "${project.name}",
    version = "${project.version}",
    description = "${project.description}",
    url = "${project.url}",
    authors = {"${author}"}
)
public class MultiProxyPlayerCount {

    private final ProxyServer server;
    private final Logger logger;
    private final Path path;

    @Inject
    public MultiProxyPlayerCount(ProxyServer server, Logger logger, @DataDirectory Path path) {
        this.server = server;
        this.logger = logger;
        this.path = path;
    }

    @Subscribe
    public void onInitialize(ProxyInitializeEvent event) {
        Config config = new ConfigImpl(path, logger);
        Database database = new DatabaseImpl(config);
        database.connectionTest().thenAccept(result -> {
            if (!result) {
                logger.warn("Connection to Redis DB failed; disabling the plugin. Reconfigure and restart server to apply changes.");
                return;
            }
            logger.info("Connection to Redis DB successful.");
            server.getScheduler().buildTask(this, () -> {
                server.getEventManager().register(this, new PlayerJoinListener(database));
                server.getEventManager().register(this, new PlayerLeaveListener(database));
                CommandManager commandManager = server.getCommandManager();
                CommandMeta commandMeta = commandManager.metaBuilder(config.getBaseCommand()).plugin(this).build();
                BrigadierCommand commandToRegister = new PluginCommand(database, config).createBrigadierCommand(server);
                commandManager.register(commandMeta, commandToRegister);
            }).schedule();
        });
    }
}
