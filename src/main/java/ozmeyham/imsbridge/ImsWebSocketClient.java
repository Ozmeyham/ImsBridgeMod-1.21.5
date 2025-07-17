package ozmeyham.imsbridge;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minidev.json.JSONObject;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.mojang.text2speech.Narrator.LOGGER;
import static ozmeyham.imsbridge.IMSBridge.*;
import static ozmeyham.imsbridge.commands.BridgeColourCommand.*;
import static ozmeyham.imsbridge.commands.CombinedBridgeColourCommand.*;
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
            // printToChat("§cConnecting to websocket...");
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
        // printToChat("§2Successfully connected to websocket.");
        // Send bridgeKey immediately after connecting
        if (bridgeKey != null) {
            wsClient.send("{\"from\":\"mc\",\"key\":" + quote(bridgeKey) + "}");
        }
    }
    private void bridgeMessage(String chatMsg, String username, String guild) {
        String colouredMsg = bridgeC1 + (guild == null ? "Bridge" : guild) + " > " + bridgeC2 + username + ": " + bridgeC3 + chatMsg;
        // Send formatted message in client chat
        if (bridgeEnabled == true) {
            MinecraftClient.getInstance().execute(() ->
                    MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal(colouredMsg))
            );
        }
    }

    private void cbridgeMessage(String chatMsg, String username, String guild) {
        String colouredMsg = cbridgeC1 + "CBridge > " + cbridgeC2 + username + ": " + cbridgeC3 + chatMsg;
        // Send formatted message in client chat
        if (combinedBridgeEnabled == true) {
            MinecraftClient.getInstance().execute(() ->
                    MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal(colouredMsg))
            );
        }
    }

    @Override
    public void onMessage(String message) {
        //printToChat(message);
        String msg = getJsonValue(message, "msg");
        String[] split = msg.split(": ", 2);
        String username = split.length > 0 ? split[0] : "";
        String chatMsg = split.length > 1 ? split[1] : "";
        String guild = GUILD_MAP.get(getJsonValue(message, "guild"));

        if (message.contains("\"combinedbridge\":true")){
            cbridgeMessage(chatMsg, username, guild);
        } else if (message.contains("\"from\":\"discord\"")){
            bridgeMessage(chatMsg, username, guild);
        }
    }

    public static String getJsonValue(String jsonString, String key) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(jsonString);
            JsonNode valueNode = node.get(key);
            return valueNode != null ? valueNode.asText() : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static final Map<String, String> GUILD_MAP = Map.ofEntries(
            Map.entry("Ironman Sweats", "IMS"),
            Map.entry("Ironman Casuals", "IMC"),
            Map.entry("Ironman Academy", "IMA")
    );

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
                // printToChat("§4Disconnected from websocket. §6Attempting to reconnect...");
                this.reconnect();
            } catch (InterruptedException e) {
                LOGGER.error("Reconnect interrupted", e);
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}