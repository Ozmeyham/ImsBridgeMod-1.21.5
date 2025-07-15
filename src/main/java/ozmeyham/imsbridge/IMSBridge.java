package ozmeyham.imsbridge;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.Command;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.io.*;
import java.nio.file.Path;
import java.util.Properties;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class IMSBridge implements ClientModInitializer {
	private static ImsWebSocketClient wsClient;
	private static final Logger LOGGER = LoggerFactory.getLogger("imsbridge");
	private static String bridgeKey = null; //

	private static final String CONFIG_FILE_NAME = "imsbridge.properties";
	private static final String CONFIG_KEY = "bridge_key";

	private boolean shouldCheckKey = false;
	private int delayTicks = 0;

	@Override
	public void onInitializeClient() {

		bridgeKey = loadBridgeKey();

		if (bridgeKey != null && !bridgeKey.isEmpty() && uuidValidator(bridgeKey)) {
			connectWebSocket();
		} else {
			// Wait for the client to fully load before printing
			ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
				shouldCheckKey = true;
				delayTicks = 40; // Wait ~2 seconds (20 ticks/sec)
			});

			// Tick handler to delay the message
			ClientTickEvents.END_CLIENT_TICK.register(client -> {
				if (shouldCheckKey && client.player != null) {
					if (delayTicks > 0) {
						delayTicks--;
					} else {
						bridgeKey = loadBridgeKey();
						if (bridgeKey == null || bridgeKey.isEmpty() || !uuidValidator(bridgeKey)) {
							printToChat("§cBridge key not set. §6Use /bridgekey {key} to connect.");
						}
						shouldCheckKey = false;
					}
				}
			});
		}

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
		// Register "/bridgekey key" command
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			dispatcher.register(LiteralArgumentBuilder.<FabricClientCommandSource>literal("bridgekey")
					.then(RequiredArgumentBuilder.<FabricClientCommandSource, String>argument("key", StringArgumentType.word())
							.executes(ctx -> {
								String key = StringArgumentType.getString(ctx, "key");
								setBridgeKey(key);
								LOGGER.info("Bridge key set to " + key);
								if (uuidValidator(bridgeKey)) {
									printToChat("§cBridge key saved as: §f" + bridgeKey);
									connectWebSocket();
								} else {
									printToChat("§cInvalid bridge key format! Check you pasted correctly.");
								}
								return Command.SINGLE_SUCCESS;
							})
					)
			);
		});
	}

	private static void setBridgeKey(String key) {
		bridgeKey = key;
		saveBridgeKey(key); // persist change
	}

	public static boolean uuidValidator(String uuid) {
		String regex = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$";

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(uuid);
		return matcher.matches();
	}

	// Config file stuff, needs refactoring
	private static Path getConfigPath() {
		return MinecraftClient.getInstance().runDirectory.toPath().resolve("config").resolve(CONFIG_FILE_NAME);
	}

	private static String loadBridgeKey() {
		Path path = getConfigPath();
		Properties props = new Properties();
		if (path.toFile().exists()) {
			try (InputStream in = new FileInputStream(path.toFile())) {
				props.load(in);
				String value = props.getProperty(CONFIG_KEY);
				if (value != null && !value.isEmpty()) {
					LOGGER.info("Loaded bridge key from config.");
					return value;
				}
			} catch (IOException e) {
				LOGGER.error("Failed to load bridge key from config.", e);
			}
		}
		return null;
	}

	private static void saveBridgeKey(String key) {
		Path path = getConfigPath();
		Properties props = new Properties();
		props.setProperty(CONFIG_KEY, key);
		try {
			File configDir = path.getParent().toFile();
			if (!configDir.exists()) configDir.mkdirs();
			try (OutputStream out = new FileOutputStream(path.toFile())) {
				props.store(out, "IMSBridge configuration");
				LOGGER.info("Saved bridge key to config.");
			}
		} catch (IOException e) {
			LOGGER.error("Failed to save bridge key to config.", e);
		}
	}

	private static void connectWebSocket() {
		if (wsClient == null || !wsClient.isOpen()) {
			printToChat("§cConnecting to websocket...");
			try {
				wsClient = new ImsWebSocketClient(new URI("wss://ims-bridge.com"));
				wsClient.connect();
			} catch (URISyntaxException e) {
				LOGGER.error("Invalid WebSocket URI", e);
			}
		}
	}
	// Simple in-game chat print because the command is so long for some reason
	public static void printToChat(String msg) {
		MinecraftClient.getInstance().execute(() ->
				MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal("§6[IMS-Bridge] " + msg))
		);
	}

	// Lightweight JSON string escaper
	public static String quote(String s) {
		return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
	}

	// Inner class for websocket
	static class ImsWebSocketClient extends WebSocketClient {
		public ImsWebSocketClient(URI serverUri) {
			super(serverUri);
		}

		@Override
		public void onOpen(ServerHandshake handshakedata) {
			LOGGER.info("WebSocket Connected");
			printToChat("§cSuccessfully connected to websocket.");
			// Send bridgeKey immediately after connecting
			if (bridgeKey != null) {
				wsClient.send("{\"from\":\"mc\",\"key\":" + quote(bridgeKey) + "}");
			}
		}

		@Override
		public void onMessage(String message) {
			// Expecting JSON {"from":"discord","msg":"Minemon205: Hi chat"}
			if (message.contains("\"from\":\"discord\"")) {
				String msg = extractMsg(message);
				String[] split = msg.split(": ", 2);
				String username = split.length > 0 ? split[0] : "";
				String chatMsg = split.length > 1 ? split[1] : "";
				String colouredMsg = "§9Bridge > §6" + username + "§f: " + chatMsg;
				// Send formatted message in client chat
				MinecraftClient.getInstance().execute(() ->
						MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal(colouredMsg))
				);
			}
		}

		// Basic extraction for {"from":"discord","msg":"something"}
		private String extractMsg(String json) {
			int i = json.indexOf("\"msg\":\"");
			if (i == -1) return "";
			i += 7;
			int j = json.indexOf("\"", i);
			if (j == -1) return json.substring(i);
			return json.substring(i, j);
		}


		@Override
		public void onClose(int code, String reason, boolean remote) {
			LOGGER.info("WebSocket Closed: {}", reason);

			if ("Invalid bridge key".equals(reason)) {
				printToChat("§cConnection failed: Invalid bridge key. §6Use /bridgekey {key} to try again.");
				LOGGER.warn("Not reconnecting due to invalid key.");
				return; // Don't attempt to reconnect
			}

			if (bridgeKey != null) {
				tryReconnecting();
			}
		}

		@Override
		public void onError(Exception ex) {
			LOGGER.error("WebSocket Error", ex);
		}
		
		private void tryReconnecting() {
            new Thread(() -> {
                try {
                    Thread.sleep(3000);
                    LOGGER.info("Attempting to reconnect...");
					printToChat("§cDisconnected from websocket. §6Attempting to reconnect...");
					this.reconnect();
                } catch (InterruptedException e) {
                    LOGGER.error("Reconnect interrupted", e);
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
	}
}