package top.nanboom233.Mixins;

import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.nanboom233.Utils.Packets.PacketManager;

@Mixin(ClientCommonNetworkHandler.class)
public class MixinClientCommonNetworkHandler {

    @Inject(
            method = "sendPacket",
            at = @At("HEAD"),
            cancellable = true
    )
    private void beforePacket(Packet<?> packet, CallbackInfo ci) {
        PacketManager.onSendPacket(packet, ci);
    }

    @Inject(
            method = "sendPacket",
            at = @At("TAIL")
    )
    private void afterPacket(Packet<?> packet, CallbackInfo ci) {
        PacketManager.onAfterPacket(packet);
    }
}
