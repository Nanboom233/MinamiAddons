package top.nanboom233.Utils.Packets;

import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.KeepAliveC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientTickEndC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.nanboom233.Features.World.EasyPlaceFix;

public class PacketManager {
    public static void onSendPacket(Packet<?> packet, CallbackInfo ci) {
        if (packet instanceof ClientTickEndC2SPacket || packet instanceof KeepAliveC2SPacket) {
            return;
        }
//        if (packet instanceof PlayerInteractBlockC2SPacket) {
//            System.out.println("actual :" + ((PlayerInteractBlockC2SPacket) packet).getBlockHitResult().getSide());
//        }
        if (packet instanceof PlayerMoveC2SPacket.LookAndOnGround povPacket) {
//            System.err.println("Sent yaw:" + povPacket.getYaw(2131));
//            System.err.println("Sent pitch:" + povPacket.getPitch(666));
            if (EasyPlaceFix.povRecovery) {
                ci.cancel();
            }
        }

//        System.err.println("Sent packet: " + packet);

    }

    public static void onAfterPacket(Packet<?> packet) {
    }
}
