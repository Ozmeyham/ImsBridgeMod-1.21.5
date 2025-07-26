package ozmeyham.imsbridge.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static ozmeyham.imsbridge.commands.BridgeColourCommand.bridgeColourCommand;
import static ozmeyham.imsbridge.commands.BridgeCommand.bridgeToggleCommand;
import static ozmeyham.imsbridge.commands.BridgeKeyCommand.bridgeKeyCommand;
import static ozmeyham.imsbridge.commands.CombinedBridgeChatCommand.combinedBridgeChatCommand;
import static ozmeyham.imsbridge.commands.CombinedBridgeChatCommand.combinedBridgeChatCommandShort;
import static ozmeyham.imsbridge.commands.CombinedBridgeColourCommand.combinedBridgeColourCommand;
import static ozmeyham.imsbridge.commands.CombinedBridgeMsgCommand.combinedBridgeMsgCommand;
import static ozmeyham.imsbridge.commands.BridgeOnlineCommand.*;
import static ozmeyham.imsbridge.commands.CombinedBridgeToggleCommand.combinedBridgeToggleCommand;
import static ozmeyham.imsbridge.commands.BridgeHelpCommand.bridgeHelpCommand;
import static ozmeyham.imsbridge.commands.CombinedBridgeHelpCommand.*;

public class CommandHandler {
    public static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        bridgeKeyCommand(dispatcher);
        bridgeToggleCommand(dispatcher);
        bridgeColourCommand(dispatcher);
        combinedBridgeToggleCommand(dispatcher);
        combinedBridgeChatCommand(dispatcher);
        combinedBridgeChatCommandShort(dispatcher);
        combinedBridgeMsgCommand(dispatcher);
        combinedBridgeColourCommand(dispatcher);
        bridgeOnlineCommand(dispatcher);
        bridgeOnlineCommandShort(dispatcher);
        bridgeHelpCommand(dispatcher);
        combinedBridgeHelpCommand(dispatcher);
    }
}