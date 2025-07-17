package ozmeyham.imsbridge.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class TextUtils {
    // Simple in-game chat print because the command is so long for some reason
    public static void printToChat(String msg) {
        MinecraftClient.getInstance().execute(() ->
                MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal("§aIMS-Bridge Mod > §r" + msg))
        );
    }

    // Lightweight JSON string escaper
    public static String quote(String s) {
        return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }
}
