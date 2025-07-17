package ozmeyham.imsbridge.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;


import static ozmeyham.imsbridge.IMSBridge.combinedbridgechatEnabled;

import static ozmeyham.imsbridge.utils.TextUtils.printToChat;

public class CombinedBridgeChatCommand {
    public static void combinedBridgeChatCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(LiteralArgumentBuilder.<FabricClientCommandSource>literal("cbridge")
                .then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("chat")
                        .executes(ctx -> {
                            if (combinedbridgechatEnabled == false) {
                                combinedbridgechatEnabled = true;
                                printToChat("§aEnabled Combined Bridge Chat!");
                            } else {
                                combinedbridgechatEnabled = false;
                                printToChat("§cDisabled Combined Bridge Chat!");
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                ));
    }
}
