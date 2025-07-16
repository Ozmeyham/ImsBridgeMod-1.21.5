package ozmeyham.imsbridge.utils;

import net.minecraft.client.MinecraftClient;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import static com.mojang.text2speech.Narrator.LOGGER;

import static ozmeyham.imsbridge.utils.BridgeKeyUtils.*;
import static ozmeyham.imsbridge.commands.BridgeColourCommand.*;
import static ozmeyham.imsbridge.IMSBridge.*;

public class ConfigUtils {

    public static final String CONFIG_FILE_NAME = "imsbridge.properties";

    public static Path getConfigPath() {
        return MinecraftClient.getInstance().runDirectory.toPath().resolve("config").resolve(CONFIG_FILE_NAME);
    }

    public static void loadConfig() {
        bridgeKey = loadConfigValue("bridgeKey", null);
        c1 = loadConfigValue("bridge_colour1","§9");
        c2 = loadConfigValue("bridge_colour2","§6");
        c3 = loadConfigValue("bridge_colour3","§f");
        bridgeEnabled = Boolean.valueOf(loadConfigValue("bridgeEnabled","true"));
    }

    public static String loadConfigValue(String CONFIG_KEY, String DEFAULT_VALUE) {
        Path path = getConfigPath();
        Properties props = new Properties();
        if (path.toFile().exists()) {
            try (InputStream in = new FileInputStream(path.toFile())) {
                props.load(in);
                String value = props.getProperty(CONFIG_KEY, DEFAULT_VALUE);
                if (value != null && !value.isEmpty()) {
                    LOGGER.info("Loaded value from config.");
                    return value;
                }
            } catch (IOException e) {
                LOGGER.error("Failed to load value from config.", e);
            }
        }
        return null;
    }

    public static void saveConfigValue(String CONFIG_KEY, String value) {
        Path path = getConfigPath();
        Properties props = new Properties();

        if (Files.exists(path)) {
            try (InputStream in = new FileInputStream(path.toFile())) {
                props.load(in);
            } catch (IOException e) {
                LOGGER.error("Failed to load existing config.", e);
            }
        }

        props.setProperty(CONFIG_KEY, value);
        try {
            File configDir = path.getParent().toFile();
            if (!configDir.exists()) configDir.mkdirs();
            try (OutputStream out = new FileOutputStream(path.toFile())) {
                props.store(out, "IMSBridge configuration");
                LOGGER.info("Saved value to config.");
            }
        } catch (IOException e) {
            LOGGER.error("Failed to save value to config.", e);
        }
    }
}
