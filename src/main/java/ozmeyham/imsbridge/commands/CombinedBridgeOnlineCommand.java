package ozmeyham.imsbridge.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static ozmeyham.imsbridge.ImsWebSocketClient.wsClient;

public class CombinedBridgeOnlineCommand {
    public static void combinedBridgeOnlineCommand (CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(LiteralArgumentBuilder.<FabricClientCommandSource>literal("cbridge")
                .then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("online")
                        .executes(ctx -> {
                            wsClient.send("{\"from\":\"mc\",\"request\":\"getOnlinePlayers\"}");
                            return Command.SINGLE_SUCCESS;
                        })
                ));
    }
    public static void combinedBridgeOnlineCommandShort (CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(LiteralArgumentBuilder.<FabricClientCommandSource>literal("bl")
                    .executes(ctx -> {
                        wsClient.send("{\"from\":\"mc\",\"request\":\"getOnlinePlayers\"}");
                        return Command.SINGLE_SUCCESS;
                    })
            );
    }
}
