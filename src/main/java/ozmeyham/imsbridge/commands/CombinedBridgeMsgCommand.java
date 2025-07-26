package ozmeyham.imsbridge.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static ozmeyham.imsbridge.IMSBridge.combinedBridgeEnabled;
import static ozmeyham.imsbridge.ImsWebSocketClient.wsClient;
import static ozmeyham.imsbridge.utils.TextUtils.*;

public class CombinedBridgeMsgCommand {
    public static void combinedBridgeMsgCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(LiteralArgumentBuilder.<FabricClientCommandSource>literal("cbc")
                .then(argument("message", StringArgumentType.greedyString())
                        .executes(ctx -> {
                            String message = StringArgumentType.getString(ctx, "message");
                            if (combinedBridgeEnabled && wsClient.isOpen() && wsClient != null) {
                                wsClient.send("{\"from\":\"mc\",\"msg\":\"" + sanitizeMessage(message) + "\",\"combinedbridge\":true}");
                            }
                            else if (wsClient == null || wsClient.isClosed()){
                                printToChat("§cYou are not connected to the bridge server!");
                            } else {
                                printToChat("§cYou need to enable cbridge messages before using cbridge! §6§oDo /cbridge toggle");
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                ));
    }
}
