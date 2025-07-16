package ozmeyham.imsbridge;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ozmeyham.imsbridge.ImsWebSocketClient.wsClient;
import static ozmeyham.imsbridge.commands.BridgeColourCommand.*;
import static ozmeyham.imsbridge.commands.BridgeCommand.*;
import static ozmeyham.imsbridge.commands.BridgeHelpCommand.*;
import static ozmeyham.imsbridge.commands.BridgeKeyCommand.*;
import static ozmeyham.imsbridge.utils.BridgeKeyUtils.*;
import static ozmeyham.imsbridge.utils.ConfigUtils.loadConfig;
import static ozmeyham.imsbridge.utils.TextUtils.quote;

public class IMSBridge implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("imsbridge");
	public static Boolean bridgeEnabled = true; // enable/disable seeing bridge messages

	@Override
	public void onInitializeClient() {

		loadConfig();
		checkBridgeKey();

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
		// Register /bridge help" command to explain command usage.
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> bridgeHelpCommand(dispatcher));
	}
}