package top.nanboom233.Packets;

import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;

public class PacketManager {
    public static void onSendPacket(Packet<?> packet) {
        if (packet instanceof PlayerInteractBlockC2SPacket) {
            System.out.println("actual :" + ((PlayerInteractBlockC2SPacket) packet).getBlockHitResult().getSide());
        }
    }

    public static void onAfterPacket(Packet<?> packet) {
    }
}
