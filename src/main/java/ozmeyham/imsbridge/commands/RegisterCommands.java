package ozmeyham.imsbridge.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static ozmeyham.imsbridge.commands.BridgeColourCommand.bridgeColourCommand;
import static ozmeyham.imsbridge.commands.BridgeCommand.bridgeToggleCommand;
import static ozmeyham.imsbridge.commands.BridgeHelpCommand.bridgeHelpCommand;
import static ozmeyham.imsbridge.commands.BridgeKeyCommand.bridgeKeyCommand;
import static ozmeyham.imsbridge.commands.CombinedBridgeChatCommand.combinedBridgeChatCommand;
import static ozmeyham.imsbridge.commands.CombinedBridgeChatCommand.combinedBridgeChatCommandShort;
import static ozmeyham.imsbridge.commands.CombinedBridgeColourCommand.combinedBridgeColourCommand;
import static ozmeyham.imsbridge.commands.CombinedBridgeHelpCommand.combinedBridgeHelpCommand;
import static ozmeyham.imsbridge.commands.CombinedBridgeMsgCommand.combinedBridgeMsgCommand;
import static ozmeyham.imsbridge.commands.CombinedBridgeOnlineCommand.*;
import static ozmeyham.imsbridge.commands.CombinedBridgeToggleCommand.combinedBridgeToggleCommand;

public class RegisterCommands {
    public static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        bridgeKeyCommand(dispatcher);
        bridgeToggleCommand(dispatcher);
        bridgeColourCommand(dispatcher);
        bridgeHelpCommand(dispatcher);
        combinedBridgeToggleCommand(dispatcher);
        combinedBridgeChatCommand(dispatcher);
        combinedBridgeChatCommandShort(dispatcher);
        combinedBridgeMsgCommand(dispatcher);
        combinedBridgeColourCommand(dispatcher);
        combinedBridgeHelpCommand(dispatcher);
        combinedBridgeOnlineCommand(dispatcher);
        combinedBridgeOnlineCommandShort(dispatcher);
    }
}