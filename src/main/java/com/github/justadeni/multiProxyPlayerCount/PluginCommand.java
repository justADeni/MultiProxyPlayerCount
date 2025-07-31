package com.github.justadeni.multiProxyPlayerCount;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class PluginCommand {

    public static BrigadierCommand createBrigadierCommand(final ProxyServer proxy) {
        LiteralCommandNode<CommandSource> helloNode = BrigadierCommand.literalArgumentBuilder("proxylist")
                .requires(source -> source instanceof ConsoleCommandSource || source.hasPermission("mppc.use"))
                .executes(context -> {
                    Thread.ofVirtual().start(() -> {
                        CommandSource source = context.getSource();

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
