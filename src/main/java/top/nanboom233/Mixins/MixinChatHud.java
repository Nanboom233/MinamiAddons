package top.nanboom233.Mixins;

import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import top.nanboom233.Config.Config;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static top.nanboom233.MinamiAddons.config;

@Mixin(ChatHud.class)
public class MixinChatHud {
    @Shadow
    @Final
    List<ChatHudLine.Visible> visibleMessages;
    @Shadow
    @Final
    List<ChatHudLine> messages;

    @ModifyVariable(
            method = "Lnet/minecraft/client/gui/hud/ChatHud;addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V",
            at = @At("HEAD"),
            argsOnly = true
    )
    private Text insertTimestamp(Text message, Text parameterMessage, @Nullable MessageSignatureData signatureData, @Nullable MessageIndicator indicator) {
        if (!config.chatTimestamp) {
            return message;
        }
        if (indicator != null && Config.timestampWhitelist.contains(indicator)) {
            return message;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm]");
        String timestamp = sdf.format(new Date(System.currentTimeMillis()));
        MutableText messageWithTimestamp = Text.literal("§7" + timestamp + " ");
        messageWithTimestamp.append(message);
        return messageWithTimestamp;
    }

}
