package top.nanboom233.Features.Miscellaneous;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHudLine;
import top.nanboom233.Config.Config;
import top.nanboom233.MinamiAddons;
import top.nanboom233.Mixins.MixinChatHudAccessor;
import top.nanboom233.Utils.Texts.ChatUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static top.nanboom233.MinamiAddons.mc;

public class ChatCopy {

    public static void regularCopy() {
        ChatHudLine text = getChatLine(getMouseX(), getMouseY());
        if (text == null) {
            return;
        }
        if (Config.copyWhiteList.contains(text.indicator())) {
            return;
        }
        mc.keyboard.setClipboard(text.content().getString().replaceAll("[\\u00a7&][0-9a-zA-Z]", ""));
        ChatUtils.debug("Copied to clipboard!", ChatUtils.MessageCategory.DEBUG);
    }

    public static void styledCopy() {
        ChatHudLine text = getChatLine(getMouseX(), getMouseY());
        if (text == null) {
            return;
        }
        if (Config.copyWhiteList.contains(text.indicator())) {
            return;
        }
        Pattern pattern = Pattern.compile("((?<!\\\\))\u00a7(?![^0-9a-fklmnor]|$)");
        Matcher matcher = pattern.matcher(ChatUtils.getOldFashionedText(text.content()));
        mc.keyboard.setClipboard(matcher.replaceAll("&"));
        ChatUtils.debug("Copied to clipboard with styles!", ChatUtils.MessageCategory.DEBUG);
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
