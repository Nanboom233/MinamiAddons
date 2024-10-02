package top.nanboom233.Utils.Texts;

import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.text.Text;
import top.nanboom233.Mixins.MixinChatHudAccessor;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static top.nanboom233.MinamiAddons.mc;
import static top.nanboom233.Utils.Texts.MinamiStyles.*;


public class ChatUtils {
    private static final Text description = new MinamiTextComponent("Messages shown by ").addStyles(GRAY)
            .append(new MinamiTextComponent("MinamiAddons").addStyles(BOLD, GOLD))
            .append(new MinamiTextComponent(".")).getText();
    public static final MessageIndicator minamiMainIndicater = new MessageIndicator(
            233333, null,
            description, "MinamiAddons");

    public static final MessageIndicator deletedIndicater = new MessageIndicator(
            0, null,
            Text.literal("This message was deteled."), "MinamiAddons");

    public static void show(MinamiTextComponent text) {
        mc.inGameHud.getChatHud().addMessage(text.getText(), null, minamiMainIndicater);
    }

    public static void show(MinamiTextComponent text, MessageIndicator indicator) {
        mc.inGameHud.getChatHud().addMessage(text.getText(), null, indicator);
    }

    public static void emptyLine() {
        mc.inGameHud.getChatHud().addMessage(Text.literal(""), null, minamiMainIndicater);
    }

    public static void emptyLine(MessageIndicator indicator) {
        mc.inGameHud.getChatHud().addMessage(Text.literal(""), null, indicator);
    }

    public static void debug(String message, MessageCategory messageCategory) {
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
        show(new MinamiTextComponent(message));
    }

    public static String getOldFashionedText(Text styledText) {
        AtomicBoolean flag = new AtomicBoolean(false);
        StringBuilder oldFashionedText = new StringBuilder();
        styledText.visit((style, text) -> {
            if (style.getColor() != null) {
                MinamiStyles color = MinamiStyles.getStyle(style.getColor().getName());
                if (color != null) {
                    oldFashionedText.append(color.FORMATTING);
                } else {
                    oldFashionedText.append("§[#").append(style.getColor().getName()).append("]");
                }
            }
            if (style.isObfuscated()) {
                flag.set(true);
                oldFashionedText.append(MinamiStyles.OBFUSCATED.FORMATTING);
            }
            if (style.isItalic()) {
                flag.set(true);
                oldFashionedText.append(MinamiStyles.ITALIC.FORMATTING);
            }
            if (style.isBold()) {
                flag.set(true);
                oldFashionedText.append(BOLD.FORMATTING);
            }
            if (style.isStrikethrough()) {
                flag.set(true);
                oldFashionedText.append(MinamiStyles.STRIKE_THROUGH.FORMATTING);
            }
            if (style.isUnderlined()) {
                flag.set(true);
                oldFashionedText.append(MinamiStyles.UNDERLINED.FORMATTING);
            }
            if (flag.get() &&
                    style.getColor() == null &&
                    !style.isUnderlined() &&
                    !style.isItalic() &&
                    !style.isBold() &&
                    !style.isStrikethrough() &&
                    !style.isStrikethrough() &&
                    !style.isObfuscated()) {
                oldFashionedText.append(RESET.FORMATTING);
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

    public static void actionbarShow(MinamiTextComponent text, boolean tinted) {
        if (mc.world != null) {
            mc.inGameHud.setOverlayMessage(text.getText(), tinted);
        }
    }

    public static void send(String message) {
        if (mc.player == null) {
            return;
        }
        if (message.startsWith("/")) {
            mc.player.networkHandler.sendChatCommand(message.substring(1));
        } else {
            mc.player.networkHandler.sendChatMessage(message);
        }
    }

    public static void removeMessage(MessageIndicator indicator) {
        MixinChatHudAccessor chatHud = (MixinChatHudAccessor) mc.inGameHud.getChatHud();
        chatHud.getMessages().removeIf(chatHudLine -> indicator.equals(chatHudLine.indicator()));
        chatHud.invokeReset();
    }
}