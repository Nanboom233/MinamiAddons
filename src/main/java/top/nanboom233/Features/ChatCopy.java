package top.nanboom233.Features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHudLine;
import top.nanboom233.MinamiAddons;
import top.nanboom233.Mixins.MixinChatHudAccessor;
import top.nanboom233.Utils.InfoUtils;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static top.nanboom233.MinamiAddons.mc;

public class ChatCopy {
    public static void regularCopy() {
        ChatHudLine text = getChatLine(getMouseX(), getMouseY());
        if (text == null) {
            return;
        }
        if (text.indicator() == InfoUtils.minamiIndicater) {
            return;
        }
        mc.keyboard.setClipboard(text.content().getString().replaceAll("[\\u00a7&][0-9a-zA-Z]", ""));
        InfoUtils.showInChat("Copied to clipboard!", InfoUtils.MessageCategory.DEBUG);
    }

    public static void styledCopy() {
        ChatHudLine text = getChatLine(getMouseX(), getMouseY());
        if (text == null) {
            return;
        }
        if (text.indicator() == InfoUtils.minamiIndicater) {
            return;
        }
        Pattern pattern = Pattern.compile("((?<!\\\\))\u00a7(?![^0-9a-fklmnor]|$)");
        Matcher matcher = pattern.matcher(getOldFashionedText(text));
        mc.keyboard.setClipboard(matcher.replaceAll("&"));
        InfoUtils.showInChat("Copied to clipboard with styles!", InfoUtils.MessageCategory.DEBUG);
    }

    public static String getOldFashionedText(ChatHudLine line) {
        StringBuilder oldFashionedText = new StringBuilder();
        line.content().visit((style, text) -> {
            if (style.getColor() != null) {
                InfoUtils.Styles color = InfoUtils.Styles.getStyle(style.getColor().getName());
                System.out.println(color);
                if (color != null) {
                    oldFashionedText.append(color.FORMATTING);
                } else {
                    oldFashionedText.append("§[#").append(style.getColor().getName()).append("]");
                }
            }
            if (style.isObfuscated()) {
                oldFashionedText.append(InfoUtils.Styles.OBFUSCATED.FORMATTING);
            }
            if (style.isItalic()) {
                oldFashionedText.append(InfoUtils.Styles.ITALIC.FORMATTING);
            }
            if (style.isBold()) {
                oldFashionedText.append(InfoUtils.Styles.BOLD.FORMATTING);
            }
            if (style.isStrikethrough()) {
                oldFashionedText.append(InfoUtils.Styles.STRIKE_THROUGH.FORMATTING);
            }
            if (style.isUnderlined()) {
                oldFashionedText.append(InfoUtils.Styles.UNDERLINED.FORMATTING);
            }
            oldFashionedText.append(text);
            return Optional.empty();
        }, line.content().getStyle());
        return oldFashionedText.toString();
    }

    private static ChatHudLine getChatLine(double x, double y) {
        MinecraftClient mc = MinamiAddons.mc;
        MixinChatHudAccessor chatHud = (MixinChatHudAccessor) mc.inGameHud.getChatHud();
        double chatLineX = chatHud.invokeToChatLineX(x);
        double chatLineY = chatHud.invokeToChatLineY(y);
        int chatIndex = chatHud.invokeGetMessageLineIndex(chatLineX, chatLineY);
        if (chatIndex >= 0 && chatIndex < chatHud.getVisibleMessages().size()) {
            List<ChatHudLine.Visible> visibleMessages = chatHud.getVisibleMessages().subList(0, chatIndex + 1);
            for (ChatHudLine.Visible visibleMessage : visibleMessages) {
                if (!visibleMessage.endOfEntry()) {
                    chatIndex--;
                }
            }
            return chatHud.getMessages().get(chatIndex);
        }
        return null;
    }

    private static double getMouseX() {
        return (int) (mc.mouse.getX() * (double) mc.getWindow().getScaledWidth() / (double) mc.getWindow().getWidth());
    }

    private static double getMouseY() {
        return (int) (mc.mouse.getY() * (double) mc.getWindow().getScaledHeight() / (double) mc.getWindow().getHeight());
    }

}
