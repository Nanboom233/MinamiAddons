package top.nanboom233.Utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.world.World;

/**
 * @author Nanboom233
 */
public class InfoUtils {
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    public static void showInChat(String message) {
        mc.inGameHud.getChatHud().addMessage(Text.of(message));
    }

    public static void showInChat(String message, MessageCategory messageCategory) {
        String messagePrefix = "[UNKNOWN] ", messagePrefixColor = "§8";
        switch (messageCategory) {
            case FATAL -> {
                messagePrefixColor = "§4";
                messagePrefix = "[FATAL] ";
            }
            case ERROR -> {
                messagePrefixColor = "§c";
                messagePrefix = "[X] ";
            }
            case WARNING -> {
                messagePrefixColor = "§e";
                messagePrefix = "[!] ";
            }
            case DEBUG -> {
                messagePrefixColor = "§d";
                messagePrefix = "[?] ";
            }
            case INFO -> {
                messagePrefixColor = "§f";
                messagePrefix = "[-] ";
            }
            default -> {
            }
        }
        message = messagePrefixColor + messagePrefix + message;
        showInChat(message);
    }


    public enum MessageCategory {
        FATAL, ERROR, WARNING, DEBUG, INFO
    }

    public static void printActionbarMessage(String key, Object... args) {
        sendVanillaMessage(Text.translatable(key, args));
    }

    public static void sendVanillaMessage(MutableText message) {
        World world = mc.world;

        if (world != null) {
            mc.inGameHud.setOverlayMessage(message, false);
        }
    }
}