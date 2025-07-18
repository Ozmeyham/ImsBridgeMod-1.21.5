package ozmeyham.imsbridge.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;


import static ozmeyham.imsbridge.IMSBridge.combinedBridgeChatEnabled;

import static ozmeyham.imsbridge.IMSBridge.combinedBridgeEnabled;
import static ozmeyham.imsbridge.utils.ConfigUtils.saveConfigValue;
import static ozmeyham.imsbridge.utils.TextUtils.printToChat;

public class CombinedBridgeChatCommand {
    public static void combinedBridgeChatCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(LiteralArgumentBuilder.<FabricClientCommandSource>literal("cbridge")
                .then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("chat")
                        .executes(ctx -> {
                            if (combinedBridgeChatEnabled == false) {
                                    combinedBridgeChatEnabled = true;
                                    saveConfigValue("combinedBridgeChatEnabled", "true");
                                    printToChat("§aEntered cbridge chat! §e§oThis functions like /chat guild, but for cbridge.");
                            } else {
                                combinedBridgeChatEnabled = false;
                                saveConfigValue("combinedBridgeChatEnabled", "false");
                                printToChat("§cExited cbridge chat!");
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                ));
    }
    public static void combinedBridgeChatCommandShort(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(LiteralArgumentBuilder.<FabricClientCommandSource>literal("cb")
                        .executes(ctx -> {
                            if (combinedBridgeChatEnabled == false) {
                                combinedBridgeChatEnabled = true;
                                saveConfigValue("combinedBridgeChatEnabled", "true");
                                printToChat("§aEntered cbridge chat! §e§oThis functions like /chat guild, but for cbridge.");
                            } else {
                                combinedBridgeChatEnabled = false;
                                saveConfigValue("combinedBridgeChatEnabled", "false");
                                printToChat("§cExited cbridge chat!");
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                );
    }
}
