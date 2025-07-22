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
                    printToChat( "§6§l- IMS Combined Bridge Help +"+
                            "\n/imsbridge bridge: Shows a list of commands for /bridge."+
                            "\n/imsbridge cbridge: Shows a list of commands for /cbridge."+
                            "\n/imsbridge bridgekey: Explains how to setup a bridge-key."
                            );
                    return Command.SINGLE_SUCCESS;
                })
        );
        dispatcher.register(LiteralArgumentBuilder.<FabricClientCommandSource>literal("imshelp")
                .then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("bridge")
                        .executes(ctx -> {
                            printToChat("§6§l- IMS Bridge Help +"+"\n§8Note that you have to be logged on Hypixel for these commands to work"+
                                    "\n§c/bridgekey <key>: §fSets your bridge key so that you can use the mod.\n" +
                                    "§c/bridge toggle: §fEnables/disables client-side bridge message rendering.\n" +
                                    "§c/bridge colour <colour1> <colour2> <colour3>: §fSets the colour formatting of rendered bridge messages.\n" +
                                    "§c/bridge colour: §fSets the colour formatting back to default.\n"
                            );
                            return Command.SINGLE_SUCCESS;
                        })
                ))
        ;
        dispatcher.register(LiteralArgumentBuilder.<FabricClientCommandSource>literal("imshelp")
                .then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("cbridge")
                        .executes(ctx -> {
                            printToChat( "§6§l- IMS Combined Bridge Help +"+
                                    "\n§c/cbridge toggle: §fEnables/disables client-side cbridge message rendering.\n" +
                                    "§c/cbridge colour <colour1> <colour2> <colour3>: §fSets the colour formatting of rendered bridge messages.\n" +
                                    "§c/cbridge colour: §fSets the colour formatting back to default.\n" +
                                    "§c/cbridge chat: §6(alias /cbc without a message following it) §fEnable/disable sending cbridge messages with no command prefix (like /chat guild)\n" +
                                    "§c/cbc <msg>: §fSends msg to cbridge, all other connected players can see this in game.\n" +
                                    "§c/cbridge online: §6(alias /bl) §fShows a list of connected players using this mod."
                            );
                            return Command.SINGLE_SUCCESS;
                        })
                )
        );
        dispatcher.register(LiteralArgumentBuilder.<FabricClientCommandSource>literal("imshelp")
                .then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("bridgekey")
                        .executes(ctx -> {
                            printToChat( "§6§l- IMS Bridge-Key Help +"+
                                    "\n§c/bridgekey <key> to set your bridge-key\n" +
                                    "§cYou can collect your bridge-key by typing /key in the commands channel. You should receive a DM from the IMS-Bridge App\n"+
                                    "§cCollect your bridge-key from the bot's message, then enter your bridge-key after /bridgekey\n"+
                                    "§c§lYou need to have your discord account linked to your Hypixel account to collect your bridge-key!\n"
                            );
                            return Command.SINGLE_SUCCESS;
                        })
                )
        );

    }

}
