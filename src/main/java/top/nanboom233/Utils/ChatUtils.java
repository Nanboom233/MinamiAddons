package top.nanboom233.Utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;


/**
 * @author Nanboom233
 */
public class ChatUtils {
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

    public static String getOldFashionedText(Text styledText) {
        StringBuilder oldFashionedText = new StringBuilder();
        styledText.visit((style, text) -> {
            if (style.getColor() != null) {
                Styles color = Styles.getStyle(style.getColor().getName());
                System.out.println(color);
                if (color != null) {
                    oldFashionedText.append(color.FORMATTING);
                } else {
                    oldFashionedText.append("§[#").append(style.getColor().getName()).append("]");
                }
            }
            if (style.isObfuscated()) {
                oldFashionedText.append(Styles.OBFUSCATED.FORMATTING);
            }
            if (style.isItalic()) {
                oldFashionedText.append(Styles.ITALIC.FORMATTING);
            }
            if (style.isBold()) {
                oldFashionedText.append(Styles.BOLD.FORMATTING);
            }
            if (style.isStrikethrough()) {
                oldFashionedText.append(Styles.STRIKE_THROUGH.FORMATTING);
            }
            if (style.isUnderlined()) {
                oldFashionedText.append(Styles.UNDERLINED.FORMATTING);
            }
            oldFashionedText.append(text);
            return Optional.empty();
        }, styledText.getStyle());
        return oldFashionedText.toString();
    }

    public static String getCleanText(Text styledText) {
        StringBuilder cleanText = new StringBuilder();
        styledText.visit((style, text) -> {
            cleanText.append(text);
            return Optional.empty();
        }, styledText.getStyle());
        return cleanText.toString();
    }


    public enum MessageCategory {
        FATAL, ERROR, WARNING, DEBUG, INFO
    }

    public static void printActionbarMessage(String key, Object... args) {
        MutableText text = Text.translatable(key, args);
        if (mc.world != null) {
            mc.inGameHud.setOverlayMessage(text, false);
        }
    }

    public static void sendMessage(String message) {
        if (mc.player == null) {
            return;
        }
        if (message.startsWith("/")) {
            mc.player.networkHandler.sendChatCommand(message.substring(1));
        } else {
            mc.player.networkHandler.sendChatMessage(message);
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