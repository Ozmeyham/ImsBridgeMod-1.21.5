package ozmeyham.imsbridge.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static ozmeyham.imsbridge.IMSBridge.*;
import static ozmeyham.imsbridge.utils.ConfigUtils.saveConfigValue;
import static ozmeyham.imsbridge.utils.TextUtils.printToChat;

public final class BridgeCommand {
    public static void bridgeToggleCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(LiteralArgumentBuilder.<FabricClientCommandSource>literal("bridge")
                .then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("toggle")
                        .executes(ctx -> {
                            if (bridgeEnabled == true) {
                                bridgeEnabled = false;
                                saveConfigValue("bridgeEnabled", "false");
                                printToChat("§cDisabled bridge messages!");
                            } else {
                                bridgeEnabled = true;
                                saveConfigValue("bridgeEnabled", "true");
                                printToChat("§2Enabled bridge messages!");
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                ));
    }
}