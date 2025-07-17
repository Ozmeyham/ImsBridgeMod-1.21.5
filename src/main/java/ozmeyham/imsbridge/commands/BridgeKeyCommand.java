package ozmeyham.imsbridge.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static ozmeyham.imsbridge.IMSBridge.*;
import static ozmeyham.imsbridge.ImsWebSocketClient.connectWebSocket;
import static ozmeyham.imsbridge.utils.BridgeKeyUtils.*;
import static ozmeyham.imsbridge.utils.ConfigUtils.loadConfig;
import static ozmeyham.imsbridge.utils.ConfigUtils.saveConfigValue;
import static ozmeyham.imsbridge.utils.TextUtils.printToChat;

public final class BridgeKeyCommand {
    public static void bridgeKeyCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(LiteralArgumentBuilder.<FabricClientCommandSource>literal("bridgekey")
                .then(RequiredArgumentBuilder.<FabricClientCommandSource, String>argument("key", StringArgumentType.word())
                        .executes(ctx -> {
                            String key = StringArgumentType.getString(ctx, "key");
                            bridgeKey = key;
                            if (isValidBridgeKey()) {
                                saveConfigValue("bridgeKey", bridgeKey);
                                loadConfig();
                                LOGGER.info("Bridge key set to " + key);
                                printToChat("§cBridge key saved as: §f" + bridgeKey);
                                connectWebSocket();
                            } else {
                                printToChat("§cInvalid bridge key format! Check you pasted correctly.");
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                ));
    }
}
