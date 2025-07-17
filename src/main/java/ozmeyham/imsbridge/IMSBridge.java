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
import static ozmeyham.imsbridge.commands.BridgeKeyCommand.*;
import static ozmeyham.imsbridge.commands.CombinedBridgeChat.combinedBridgeChat;
import static ozmeyham.imsbridge.commands.CombinedBridgeChatCommand.combinedBridgeChatCommand;
import static ozmeyham.imsbridge.commands.CombinedBridgeCommand.disableCBCommand;
import static ozmeyham.imsbridge.commands.CombinedBridgeCommand.enableCBCommand;
import static ozmeyham.imsbridge.utils.BridgeKeyUtils.*;
import static ozmeyham.imsbridge.utils.ConfigUtils.loadConfig;
import static ozmeyham.imsbridge.utils.TextUtils.quote;

public class IMSBridge implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("imsbridge");
	public static Boolean bridgeEnabled = true; // enable/disable seeing bridge messages
	public static Boolean combinedbridgeEnabled = false;
	public static Boolean combinedbridgechatEnabled = false;

	@Override
	public void onInitializeClient() {

		loadConfig();
		checkBridgeKey();

		ClientSendMessageEvents.ALLOW_CHAT.register((message) -> {
			if (combinedbridgechatEnabled == true && wsClient != null && wsClient.isOpen() && bridgeKey != null) {
				wsClient.send("{\"from\":\"mc\",\"msg\":" + message + ",\"combinedbridge\":true}");
				return false;
			} else {
				return true;
			}
		});
		// Listen for incoming game chat messages (expression lambda)
		ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
			String content = message.getString();
			if (content.contains("§2Guild >")) {
				// Send to websocket
				if (wsClient != null && wsClient.isOpen() && bridgeKey != null) {
					wsClient.send("{\"from\":\"mc\",\"msg\":" + quote(content) + "}");
				}
			}
		});

		// Register "/bridgekey" command
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> bridgeKeyCommand(dispatcher));
		// Register "/bridge enable" command to enable receiving messages
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> bridgeEnableCommand(dispatcher));
		// Register "/bridge disable" command to disable receiving messages
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> bridgeDisableCommand(dispatcher));
		// Register "/bridge colour" command to format bridge messages.
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> bridgeColourCommand(dispatcher));
		// Register "/bridge help" command to explain command usage.
		// ^ there is no bridge help are you schizophrenic
		// Register "/combinedbridge enable" command to explain command usage.
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> enableCBCommand(dispatcher));
		// Register "/combinedbridge disable" command to explain command usage.
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> disableCBCommand(dispatcher));
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> combinedBridgeChatCommand(dispatcher));
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> combinedBridgeChat(dispatcher));

	}
}