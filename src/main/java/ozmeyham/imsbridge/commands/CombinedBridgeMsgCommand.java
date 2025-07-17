package ozmeyham.imsbridge.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static ozmeyham.imsbridge.IMSBridge.combinedBridgeEnabled;
import static ozmeyham.imsbridge.ImsWebSocketClient.wsClient;
import static ozmeyham.imsbridge.utils.TextUtils.printToChat;

public class CombinedBridgeMsgCommand {
    public static void combinedBridgeMsgCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(LiteralArgumentBuilder.<FabricClientCommandSource>literal("bc")
                .then(argument("message", StringArgumentType.greedyString())
                        .executes(ctx -> {
                            String message = StringArgumentType.getString(ctx, "message");
                            if (combinedBridgeEnabled == false) {
                                printToChat("§cYou have to enable combined bridge to use this command! §6§i/cbridge toggle");
                            } else {
                                wsClient.send("{\"from\":\"mc\",\"msg\":\"" + message + "\",\"combinedbridge\":true}");
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                ));
    }
}
