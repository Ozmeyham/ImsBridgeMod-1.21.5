package ozmeyham.imsbridge;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;

public class IMSBridge implements ClientModInitializer {
	private static ImsWebSocketClient wsClient;
	private static final Logger LOGGER = LoggerFactory.getLogger("imsbridge");

	@Override
	public void onInitializeClient() {
		try {
			wsClient = new ImsWebSocketClient(new URI("wss://ims.crabdance.com"));
			wsClient.connect();
		} catch (URISyntaxException e) {
			LOGGER.error("Invalid WebSocket URI", e);
		}

		// Listen for incoming game chat messages (expression lambda)
		ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
			String content = message.getString();
			if (content.contains("§2Guild >")) {
				// Send to websocket
				if (wsClient != null && wsClient.isOpen()) {
					wsClient.send("{\"from\":\"mc\",\"msg\":" + quote(content) + "}");
				}
			}
		});
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
		}

		@Override
		public void onMessage(String message) {
			// Expecting JSON {"from":"discord","msg":"the message"}
			if (message.contains("\"from\":\"discord\"")) {
				String msg = extractMsg(message);
				// Display in client chat
				MinecraftClient.getInstance().execute(() ->
						MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal("[Discord] " + msg))
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
			tryReconnecting();
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
                    this.reconnect();
                } catch (InterruptedException e) {
                    LOGGER.error("Reconnect interrupted", e);
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
	}
}