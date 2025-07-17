package ozmeyham.imsbridge.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static ozmeyham.imsbridge.IMSBridge.bridgeEnabled;
import static ozmeyham.imsbridge.IMSBridge.combinedbridgeEnabled;
import static ozmeyham.imsbridge.utils.ConfigUtils.saveConfigValue;
import static ozmeyham.imsbridge.utils.TextUtils.printToChat;

public class CombinedBridgeCommand {
    public static void enableCBCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(LiteralArgumentBuilder.<FabricClientCommandSource>literal("combinedbridge")
                .then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("enable")
                        .executes(ctx -> {
                            if (combinedbridgeEnabled == true) {
                                printToChat("§cCombined Bridge already enabled! Do /combinedbridge disable to disable Combined Bridge messages.");
                            } else {
                                combinedbridgeEnabled = true;
                                saveConfigValue("combinedbridgeEnabled","true");
                                printToChat("§aCombined Bridge messages enabled! §8§i/bc <message | /combinedbridge chat");
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                ));
    }
    public static void disableCBCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(LiteralArgumentBuilder.<FabricClientCommandSource>literal("combinedbridge")
                .then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("disable")
                        .executes(ctx -> {
                            if (combinedbridgeEnabled == false) {
                                printToChat("§cCombined Bridge already disabled! Do /combinedbridge enable to enable Combined Bridge messages.");
                            } else {
                                combinedbridgeEnabled = false;
                                saveConfigValue("combinedbridgeEnabled","true");
                                printToChat("§cCombined Bridge messages disabled.");
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                ));
    }
}
