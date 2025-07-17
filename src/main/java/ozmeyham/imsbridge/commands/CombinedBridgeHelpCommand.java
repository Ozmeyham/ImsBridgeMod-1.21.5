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

public final class CombinedBridgeHelpCommand {
    public static void combinedBridgeHelpCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(LiteralArgumentBuilder.<FabricClientCommandSource>literal("cbridge")
                .then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("help")
                        .executes(ctx -> {
                            printToChat(
                                    "\n§c/cbridge toggle: §fEnables/disables client-side cbridge message rendering.\n" +
                                        "§c/cbridge colour <colour1> <colour2> <colour3>: §fSets the colour formatting of rendered bridge messages.\n" +
                                        "§c/cbridge colour: §fSets the colour formatting back to default.\n" +
                                        "§c/cbridge chat: §fEnable/disable sending cbridge messages with no command prefix (like /chat guild)\n" +
                                        "§c/bc <msg>: §fSends msg to cbridge, all other connected players can see this in game.\n" +
                                        "§c/cbridge help: §fShows this message.\n"
                            );
                            return Command.SINGLE_SUCCESS;
                        })
                ));
    }
}