package ozmeyham.imsbridge.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import static ozmeyham.imsbridge.IMSBridge.*;
import static ozmeyham.imsbridge.utils.ConfigUtils.saveConfigValue;
import static ozmeyham.imsbridge.utils.TextUtils.printToChat;

public final class BridgeHelpCommand {
    public static void bridgeHelpCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(LiteralArgumentBuilder.<FabricClientCommandSource>literal("bridge")
                .then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("help")
                        .executes(ctx -> {
                            printToChat(
                                    "\n§c/bridgekey <key>: §fSets your bridge key so that you can use the mod.\n" +
                                    "§c/bridge toggle: §fEnables/disables client-side bridge message rendering.\n" +
                                    "§c/bridge colour <colour1> <colour2> <colour3>: §fSets the colour formatting of rendered bridge messages.\n" +
                                    "§c/bridge colour: §fSets the colour formatting back to default.\n" +
                                    "§c/bridge help: §fShows this message.\n"
                            );
                            return Command.SINGLE_SUCCESS;
                        })
                ));
    }
}