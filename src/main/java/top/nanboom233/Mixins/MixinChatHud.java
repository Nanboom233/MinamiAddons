package top.nanboom233.Mixins;

import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import top.nanboom233.Config.Config;

import java.text.SimpleDateFormat;
import java.util.Date;

@Mixin(ChatHud.class)
public abstract class MixinChatHud {
    @ModifyVariable(
            method = "Lnet/minecraft/client/gui/hud/ChatHud;addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V",
            at = @At("HEAD"),
            argsOnly = true
    )
    private Text insertTimestamp(Text message, Text parameterMessage, @Nullable MessageSignatureData signatureData, @Nullable MessageIndicator indicator) {
        if (!Config.getInstance().chatTimestamp) {
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
