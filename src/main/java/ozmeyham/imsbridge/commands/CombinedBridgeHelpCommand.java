package ozmeyham.imsbridge.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static ozmeyham.imsbridge.utils.TextUtils.printToChat;

public class CombinedBridgeHelpCommand {
    public static void combinedBridgeHelpCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(LiteralArgumentBuilder.<FabricClientCommandSource>literal("cbridge")
                .then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("help")
                        .executes(ctx -> {
                            printToChat("""
                                    §b§l- Combined Bridge Help -
                                    §b/cbridge toggle: §7Enables/disables cbridge message rendering.
                                    §b/cbridge colour <colour1> <colour2> <colour3>: §7Sets the colour formatting of rendered cbridge messages.
                                    §b/cbridge colour: §7Sets the colour formatting back to default.
                                    §b/cbridge chat: §6(alias /cbc without a message following it) §7Enable/disable sending cbridge messages with no command prefix (like /chat guild)
                                    §b/cbc <msg>: §7Sends msg to cbridge, all other connected players can see this in game and on discord.
                                    """
                            );
                            return Command.SINGLE_SUCCESS;
                        })
                )
        );
    }
}
