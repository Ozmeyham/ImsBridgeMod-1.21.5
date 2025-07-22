package ozmeyham.imsbridge;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.network.ClientConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ozmeyham.imsbridge.ImsWebSocketClient.wsClient;
import static ozmeyham.imsbridge.commands.RegisterCommands.registerCommands;
import static ozmeyham.imsbridge.utils.BridgeKeyUtils.*;
import static ozmeyham.imsbridge.utils.ConfigUtils.loadConfig;
import static ozmeyham.imsbridge.utils.ConfigUtils.saveConfigValue;
import static ozmeyham.imsbridge.utils.JSONSanitization.sanitizeMessage;
import static ozmeyham.imsbridge.utils.TextUtils.*;
import static ozmeyham.imsbridge.utils.UpdateChecker.checkForUpdates;

public class IMSBridge implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("imsbridge");
	public static Boolean bridgeEnabled = false; // enable/disable seeing bridge messages
	public static Boolean combinedBridgeEnabled = false; // enable/disable seeing cbridge messages
	public static Boolean combinedBridgeChatEnabled = false; // enable/disable sending cbridge messages with no command prefix (like /chat guild)
	public static Boolean onHypixel = false; // check if on hypixel yk

	@Override
	public void onInitializeClient() {
		loadConfig();
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> registerCommands(dispatcher));
		ClientSendMessageEvents.ALLOW_CHAT.register((message) -> {
			if (combinedBridgeEnabled && combinedBridgeChatEnabled && wsClient != null && wsClient.isOpen() && bridgeKey != null) {
				if (!message.startsWith("/")) {
					wsClient.send("{\"from\":\"mc\",\"msg\":\"" + sanitizeMessage(message) + "\",\"combinedbridge\":true}");
					return false;
				}
			} else if (combinedBridgeChatEnabled && !combinedBridgeEnabled) {
				printToChat("§cYou need to enable cbridge messages before using cbridge! §6§oDo /cbridge toggle");
				return false;
			}
			return true;
		});

		// Listen for outgoing guild messages and channel changes
		ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
			String content = message.getString();
			if (content.contains("§2Guild >")) {
				// Send to websocket
				if (wsClient != null && wsClient.isOpen() && bridgeKey != null) {
					wsClient.send("{\"from\":\"mc\",\"msg\":" + quote(sanitizeMessage(content)) + "}");
				}
			} else if (isSkyblockChannelChange(content) && combinedBridgeChatEnabled == true) {
				combinedBridgeChatEnabled = false;
				saveConfigValue("combinedBridgeChatEnabled", "false");
				printToChat("§cExited cbridge chat!");
			}
		});

		// Check if on Hypixel
		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			checkBridgeKey();
			checkForUpdates();
			ClientConnection connection = handler.getConnection();
			String address = connection.getAddress().toString().toLowerCase();
			onHypixel = address.contains("hypixel.net");
			if (!onHypixel) {
				printToChat("§cCertain features have been disabled since you aren't on Hypixel.");
			}
		});
	}
}