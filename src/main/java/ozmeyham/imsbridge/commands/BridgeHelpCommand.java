package ozmeyham.imsbridge.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static ozmeyham.imsbridge.utils.TextUtils.printToChat;

public class BridgeHelpCommand {
    public static void bridgeHelpCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(LiteralArgumentBuilder.<FabricClientCommandSource>literal("bridge")
                .then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("help")
                        .executes(ctx -> {
                            printToChat("""
                                   §9§l- Bridge Help -
                                   §9/bridge key <key>: §7Sets your bridge key; obtain key from discord bot.
                                   §9/bridge toggle: §7Enables/disables bridge message rendering.
                                   §9/bridge colour <colour1> <colour2> <colour3>: §7Sets the colour formatting of rendered bridge messages.
                                   §9/bridge colour: §7Sets the colour formatting back to default.
                                   §9/bridge online: §6(alias /bl) §7Shows a list of connected players using this mod.
                                   """
                            );
                            return Command.SINGLE_SUCCESS;
                        })
                )
        );
    }
}
