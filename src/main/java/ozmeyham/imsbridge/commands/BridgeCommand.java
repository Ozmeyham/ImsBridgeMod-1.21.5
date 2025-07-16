package ozmeyham.imsbridge.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static ozmeyham.imsbridge.IMSBridge.*;
import static ozmeyham.imsbridge.utils.ConfigUtils.saveConfigValue;
import static ozmeyham.imsbridge.utils.TextUtils.printToChat;

public final class BridgeCommand {
    public static void bridgeEnableCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(LiteralArgumentBuilder.<FabricClientCommandSource>literal("bridge")
                .then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("enable")
                        .executes(ctx -> {
                            if (bridgeEnabled == true) {
                                printToChat("§2Bridge already enabled! §cDo /bridge disable to disable bridge messages.");
                            } else {
                                bridgeEnabled = true;
                                saveConfigValue("bridgeEnabled","true");
                                printToChat("§2Bridge messages enabled!");
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                ));
    }

    public static void bridgeDisableCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(LiteralArgumentBuilder.<FabricClientCommandSource>literal("bridge")
                .then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("disable")
                        .executes(ctx -> {
                            if (bridgeEnabled == false) {
                                printToChat("§cBridge already disabled! Do /bridge enable to enable bridge messages.");
                            } else {
                                bridgeEnabled = false;
                                saveConfigValue("bridgeEnabled","false");
                                printToChat("§cBridge messages disabled!");
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                ));
    }
}
