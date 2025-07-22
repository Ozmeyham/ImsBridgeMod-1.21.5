package ozmeyham.imsbridge.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static ozmeyham.imsbridge.utils.TextUtils.printToChat;

public class IMSHelpCommand {
    public static void imsHelpCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(LiteralArgumentBuilder.<FabricClientCommandSource>literal("imshelp")
                .executes(ctx -> {
                    printToChat( "\n§5§l- IMS Combined Bridge Help +"+
                            "\n§d/imshelp bridge: §fShows a list of commands for /bridge."+
                            "\n§d/imshelp cbridge: §fShows a list of commands for /cbridge."+
                            "\n§d/imshelp bridgekey: §fExplains how to setup a bridge-key.\n"
                            );
                    return Command.SINGLE_SUCCESS;
                })
        );
        dispatcher.register(LiteralArgumentBuilder.<FabricClientCommandSource>literal("imshelp")
                .then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("bridge")
                        .executes(ctx -> {
                            printToChat("\n§9§l- IMS Bridge Help +"+"\n§8§oPlease note that these commands only work on Hypixel."+
                                    "\n§9/bridgekey <key>: §bSets your bridge key so that you can use the mod.\n" +
                                    "§9/bridge toggle: §bEnables/disables client-side bridge message rendering.\n" +
                                    "§9/bridge colour <colour1> <colour2> <colour3>: §bSets the colour formatting of rendered bridge messages.\n" +
                                    "§9/bridge colour: §bSets the colour formatting back to default.\n"
                            );
                            return Command.SINGLE_SUCCESS;
                        })
                ))
        ;
        dispatcher.register(LiteralArgumentBuilder.<FabricClientCommandSource>literal("imshelp")
                .then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("cbridge")
                        .executes(ctx -> {
                            printToChat( "\n§4§l- IMS Combined Bridge Help +"+
                                    "\n§4/cbridge toggle: §cEnables/disables client-side cbridge message rendering.\n" +
                                    "§4/cbridge colour <colour1> <colour2> <colour3>: §cSets the colour formatting of rendered bridge messages.\n" +
                                    "§4/cbridge colour: §cSets the colour formatting back to default.\n" +
                                    "§4/cbridge chat: §c(alias /cbc without a message following it) §cEnable/disable sending cbridge messages with no command prefix (like /chat guild)\n" +
                                    "§4/cbc <msg>: §cSends msg to cbridge, all other connected players can see this in game.\n" +
                                    "§4/cbridge online: §c(alias /bl) §cShows a list of connected players using this mod.\n"
                            );
                            return Command.SINGLE_SUCCESS;
                        })
                )
        );
        dispatcher.register(LiteralArgumentBuilder.<FabricClientCommandSource>literal("imshelp")
                .then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("bridgekey")
                        .executes(ctx -> {
                            printToChat( "\n§c§l- IMS Bridge-Key Help +"+
                                    "\n§e/bridgekey <key> to set your bridge-key\n" +
                                    "§eYou can collect your bridge-key by typing /key in the commands channel. You should receive a DM from the IMS-Bridge App\n"+
                                    "§eCollect your bridge-key from the bot's message, then enter your bridge-key after /bridgekey\n"+
                                    "§c§lYou need to have your discord account linked to your Hypixel account to get your bridge-key!\n"
                            );
                            return Command.SINGLE_SUCCESS;
                        })
                )
        );

    }

}
