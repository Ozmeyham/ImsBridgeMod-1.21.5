package ozmeyham.imsbridge.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static ozmeyham.imsbridge.IMSBridge.combinedBridgeEnabled;
import static ozmeyham.imsbridge.utils.ConfigUtils.saveConfigValue;
import static ozmeyham.imsbridge.utils.TextUtils.printToChat;

public class CombinedBridgeToggleCommand {
    public static void combinedBridgeToggleCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(LiteralArgumentBuilder.<FabricClientCommandSource>literal("cbridge")
                .then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("toggle")
                        .executes(ctx -> {
                            if (combinedBridgeEnabled == true) {
                                combinedBridgeEnabled = false;
                                saveConfigValue("combinedBridgeEnabled", "false");
                                printToChat("§cDisabled combined bridge messages!");
                            } else {
                                combinedBridgeEnabled = true;
                                saveConfigValue("combinedBridgeEnabled", "true");
                                printToChat("§aEnabled combined bridge messages!");
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                ));
    }
}
