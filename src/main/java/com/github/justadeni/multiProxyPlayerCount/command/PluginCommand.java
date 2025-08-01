package com.github.justadeni.multiProxyPlayerCount.command;

import com.github.justadeni.multiProxyPlayerCount.config.Config;
import com.github.justadeni.multiProxyPlayerCount.connection.Database;
import com.github.justadeni.multiProxyPlayerCount.connection.PlayerData;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

import java.util.Set;
import java.util.stream.Collectors;

public class PluginCommand {

    private final Database database;
    private final Config config;

    public PluginCommand(Database database, Config config) {
        this.database = database;
        this.config = config;
    }

    public BrigadierCommand createBrigadierCommand(final ProxyServer proxy) {
        LiteralCommandNode<CommandSource> mainNode = BrigadierCommand.literalArgumentBuilder("proxylist")
                .requires(source -> source.hasPermission("multiproxyplayercount.list"))
                .executes(context -> {
                    Thread.ofVirtual().start(() -> {
                        CommandSource source = context.getSource();
                        String csl = database
                                .query()
                                .join()
                                .stream()
                                .map(PlayerData::playerName)
                                .collect(Collectors.joining(", "));

                        Component component = MiniMessage.miniMessage().deserialize(config.getSimpleFormat(), Placeholder.unparsed("online_players", csl));
                        source.sendRichMessage(MiniMessage.miniMessage().serialize(component));
                    });
                    return Command.SINGLE_SUCCESS;
                })
                .then(BrigadierCommand.literalArgumentBuilder("detailed")
                        .requires(source -> source.hasPermission("multiproxyplayercount.detailed"))
                        .executes(context -> {
                            Thread.ofVirtual().start(() -> {
                                CommandSource source = context.getSource();
                                Set<PlayerData> data = database.query().join();
                            });
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .then(BrigadierCommand.literalArgumentBuilder("reload")
                        .requires(source -> source.hasPermission("multiproxyplayercount.reload"))
                        .executes(context -> {
                            Thread.ofVirtual().start(config::reload);
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .build();

        return new BrigadierCommand(mainNode);
    }

}
