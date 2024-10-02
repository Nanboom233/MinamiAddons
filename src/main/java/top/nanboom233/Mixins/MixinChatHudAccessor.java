package top.nanboom233.Mixins;

import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

/**
 * @author Nanboom233
 * @since 2024/10/3
 */
@Mixin(ChatHud.class)
public interface MixinChatHudAccessor {

    @Invoker("toChatLineX")
    double invokeToChatLineX(double x);

    @Invoker("toChatLineY")
    double invokeToChatLineY(double y);

    @Invoker("getMessageIndex")
    int invokeGetMessageIndex(double chatLineX, double chatLineY);

    @Invoker("getMessageLineIndex")
    int invokeGetMessageLineIndex(double chatLineX, double chatLineY);

    @Invoker("reset")
    void invokeReset();

    @Accessor("visibleMessages")
    List<ChatHudLine.Visible> getVisibleMessages();

    @Accessor("messages")
    List<ChatHudLine> getMessages();
}
