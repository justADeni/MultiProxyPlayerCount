package com.github.justadeni.multiProxyPlayerCount.command;

import com.github.justadeni.multiProxyPlayerCount.config.Config;
import com.github.justadeni.multiProxyPlayerCount.connection.Database;
import com.github.justadeni.multiProxyPlayerCount.connection.PlayerData;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Set;

public class PluginCommand {

    private final Database database;
    private final Config config;

    public PluginCommand(Database database, Config config) {
        this.database = database;
        this.config = config;
    }

    public BrigadierCommand createBrigadierCommand(final ProxyServer proxy) {
        LiteralCommandNode<CommandSource> helloNode = BrigadierCommand.literalArgumentBuilder("proxylist")
                .requires(source -> source instanceof ConsoleCommandSource || source.hasPermission("multiproxyplayercount.use"))
                .executes(context -> {
                    Thread.ofVirtual().start(() -> {
                        CommandSource source = context.getSource();
                        Set<PlayerData> data = database.query().join();
                        //TODO: handle parsing and displaying the data here
                        Component message = Component.text("Hello World", NamedTextColor.AQUA);
                        source.sendMessage(message);
                    });
                    return Command.SINGLE_SUCCESS;
                })
                .build();

        // BrigadierCommand implements Command
        return new BrigadierCommand(helloNode);
    }

}
