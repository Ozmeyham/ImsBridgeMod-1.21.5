package ozmeyham.imsbridge.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static ozmeyham.imsbridge.ImsWebSocketClient.wsClient;
import static ozmeyham.imsbridge.utils.TextUtils.printToChat;

public class BridgeOnlineCommand {
    public static void bridgeOnlineCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(LiteralArgumentBuilder.<FabricClientCommandSource>literal("bridge")
                .then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("online")
                        .executes(ctx -> {
                            if (wsClient != null && wsClient.isOpen()) {
                                wsClient.send("{\"from\":\"mc\",\"request\":\"getOnlinePlayers\"}");
                            } else {
                                printToChat("§cYou are not connected to the bridge server!");
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                ));
    }
    public static void bridgeOnlineCommandShort(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(LiteralArgumentBuilder.<FabricClientCommandSource>literal("bl")
                    .executes(ctx -> {
                        if (wsClient != null && wsClient.isOpen()) {
                            wsClient.send("{\"from\":\"mc\",\"request\":\"getOnlinePlayers\"}");
                        } else {
                            printToChat("§cYou are not connected to the bridge server!");
                        }
                        return Command.SINGLE_SUCCESS;
                    })
            );
    }
}
