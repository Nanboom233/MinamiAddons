package top.nanboom233.Utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;


/**
 * @author Nanboom233
 */
public class InfoUtils {
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    public static final MessageIndicator minamiIndicater = new MessageIndicator(
            13684944, null,
            Text.of("Messages shown by MinamiAddons."), "MinamiAddons");

    public static void showInChat(String message) {
        mc.inGameHud.getChatHud().addMessage(Text.of(message), null, minamiIndicater);
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

    public enum Styles {
        BLACK("§0", "black"),
        DARK_BLUE("§1", "dark_blue"),
        DARK_GREEN("§2", "dark_green"),
        DARK_AQUA("§3", "dark_aqua"),
        DARK_RED("§4", "dark_red"),
        DARK_PURPLE("§5", "dark_purple"),
        GOLD("§6", "gold"),
        GRAY("§7", "gray"),
        DARK_GRAY("§8", "dark_gray"),
        BLUE("§9", "blue"),
        GREEN("§a", "green"),
        AQUA("§b", "aqua"),
        RED("§c", "red"),
        LIGHT_PURPLE("§d", "light_purple"),
        YELLOW("§e", "yellow"),
        WHITE("§f", "white"),
        BOLD("§l", "bold"),
        ITALIC("§o", "italic"),
        UNDERLINED("§n", "underlined"),
        STRIKE_THROUGH("§m", "strike_through"),
        OBFUSCATED("§k", "obfuscated"),
        RESET("§r", "reset");


        public final String FORMATTING;
        public final String NAME;

        Styles(String formatting, String name) {
            this.FORMATTING = formatting;
            this.NAME = name;
        }

        @Nullable
        public static Styles getStyle(String identifier) {
            for (Styles style : Styles.values()) {
                if (style.FORMATTING.equals(identifier) || style.NAME.equals(identifier)) {
                    return style;
                }
            }
            return null;
        }

        public String setStyle(String text) {
            return this.FORMATTING + text;
        }
    }
}