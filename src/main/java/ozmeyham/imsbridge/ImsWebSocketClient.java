package ozmeyham.imsbridge;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

import static com.mojang.text2speech.Narrator.LOGGER;
import static ozmeyham.imsbridge.IMSBridge.*;
import static ozmeyham.imsbridge.commands.BridgeColourCommand.*;
import static ozmeyham.imsbridge.utils.BridgeKeyUtils.bridgeKey;
import static ozmeyham.imsbridge.utils.TextUtils.printToChat;
import static ozmeyham.imsbridge.utils.TextUtils.quote;

public class ImsWebSocketClient extends WebSocketClient {

    public static ImsWebSocketClient wsClient;

    public ImsWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    public static void connectWebSocket() {
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

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        LOGGER.info("WebSocket Connected");
        printToChat("§2Successfully connected to websocket.");
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
            String colouredMsg = c1 + "Bridge > " + c2 + username + ": " + c3 + chatMsg;
            // Send formatted message in client chat
            if (bridgeEnabled == true) {
                MinecraftClient.getInstance().execute(() ->
                        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal(colouredMsg))
                );
            }
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
            printToChat("§4Disconnected from websocket: failed to authenticate bridge key. §6Use /bridgekey {key} to try again.");
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
                printToChat("§4Disconnected from websocket. §6Attempting to reconnect...");
                this.reconnect();
            } catch (InterruptedException e) {
                LOGGER.error("Reconnect interrupted", e);
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}