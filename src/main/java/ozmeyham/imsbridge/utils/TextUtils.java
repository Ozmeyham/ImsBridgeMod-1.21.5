package ozmeyham.imsbridge.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.Set;

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

    public static Boolean isSkyblockChannelChange(String content) {
        Set<String> validMessages = Set.of("You must be in a party to join the party channel!",
                "You're already in this channel!",
                "You are now in the GUILD channel",
                "You are now in the ALL channel",
                "You are now in the PARTY channel",
                "You are now in the OFFICER channel",
                "You are now in the SKYBLOCK CO-OP channel");
        return validMessages.contains(content);
    }
}
