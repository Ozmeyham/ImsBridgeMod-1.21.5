package ozmeyham.imsbridge;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ozmeyham.imsbridge.ImsWebSocketClient.wsClient;
import static ozmeyham.imsbridge.commands.BridgeColourCommand.*;
import static ozmeyham.imsbridge.commands.BridgeCommand.*;
import static ozmeyham.imsbridge.commands.BridgeHelpCommand.*;
import static ozmeyham.imsbridge.commands.BridgeKeyCommand.*;
import static ozmeyham.imsbridge.commands.CombinedBridgeColourCommand.*;
import static ozmeyham.imsbridge.commands.CombinedBridgeHelpCommand.*;
import static ozmeyham.imsbridge.commands.CombinedBridgeMsgCommand.*;
import static ozmeyham.imsbridge.commands.CombinedBridgeChatCommand.*;
import static ozmeyham.imsbridge.commands.CombinedBridgeToggleCommand.*;
import static ozmeyham.imsbridge.utils.BridgeKeyUtils.*;
import static ozmeyham.imsbridge.utils.ConfigUtils.loadConfig;
import static ozmeyham.imsbridge.utils.TextUtils.quote;

public class IMSBridge implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("imsbridge");
	public static Boolean bridgeEnabled = false; // enable/disable seeing bridge messages
	public static Boolean combinedBridgeEnabled = false; // enable/disable seeing cbridge messages
	public static Boolean combinedBridgeChatEnabled = false; // enable/disable sending cbridge messages with no command prefix (like /chat guild)

	@Override
	public void onInitializeClient() {

		loadConfig();
		checkBridgeKey();

		// Listen for outgoing cbridge chat messages
		ClientSendMessageEvents.ALLOW_CHAT.register((message) -> {
			if (combinedBridgeChatEnabled == true && wsClient != null && wsClient.isOpen() && bridgeKey != null) {
				wsClient.send("{\"from\":\"mc\",\"msg\":\"" + message + "\",\"combinedbridge\":true}");
				return false;
			} else {
				return true;
			}
		});
		// Listen for outgoing guild messages
		ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
			String content = message.getString();
			if (content.contains("§2Guild >")) {
				// Send to websocket
				if (wsClient != null && wsClient.isOpen() && bridgeKey != null) {
					wsClient.send("{\"from\":\"mc\",\"msg\":" + quote(content) + "}");
				}
			}
		});

		// Register "/bridgekey <key>" command to input bridge key
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> bridgeKeyCommand(dispatcher));
		// Register "/bridge toggle" command to toggle receiving bridge messages
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> bridgeToggleCommand(dispatcher));
		// Register "/bridge colour" command to format bridge messages.
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> bridgeColourCommand(dispatcher));
		// Register "/bridge help" command to explain command usage.
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> bridgeHelpCommand(dispatcher));

		// Register "/cbridge toggle" command to toggle receiving cbridge messages.
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> combinedBridgeToggleCommand(dispatcher));
		// Register "/cbridge chat" command to toggle cbridge chat functionality
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> combinedBridgeChatCommand(dispatcher));
		// Register "/bc <msg>" command to send messages to cbridge.
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> combinedBridgeMsgCommand(dispatcher));
		// Register "/cbridge colour" command to format cbridge messages.
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> combinedBridgeColourCommand(dispatcher));
		// Register "/cbridge help" command to explain cbridge command usage.
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> combinedBridgeHelpCommand(dispatcher));
	}
}