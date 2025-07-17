package ozmeyham.imsbridge.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static ozmeyham.imsbridge.IMSBridge.combinedbridgechatEnabled;
import static ozmeyham.imsbridge.ImsWebSocketClient.wsClient;
import static ozmeyham.imsbridge.utils.TextUtils.printToChat;

public class CombinedBridgeChat {
    public static void combinedBridgeChat(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(LiteralArgumentBuilder.<FabricClientCommandSource>literal("bc")
                .then(argument("message", StringArgumentType.greedyString())
                        .executes(ctx -> {
                            if (combinedbridgechatEnabled == false) {
                                printToChat("§cYou have to enable Combined Bridge Chat to use this command! §8§i/combinedbridge enable");
                            } else {
                                wsClient.send("{\"from\":\"mc\",\"msg\":" + ctx + ",\"combinedbridge\":true}");

                            }
                            return Command.SINGLE_SUCCESS;
                        })
                ));
    }
}
