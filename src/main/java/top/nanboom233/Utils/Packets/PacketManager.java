package top.nanboom233.Utils.Packets;

import net.minecraft.network.packet.Packet;

public class PacketManager {
    public static void onSendPacket(Packet<?> packet) {
//        if (packet instanceof PlayerInteractBlockC2SPacket) {
//            System.out.println("actual :" + ((PlayerInteractBlockC2SPacket) packet).getBlockHitResult().getSide());
//        }
//        if (packet instanceof PlayerMoveC2SPacket.LookAndOnGround povPacket) {
//            System.err.println("Sent yaw:" + povPacket.getYaw(2131));
////            System.err.println("Sent pitch:" + povPacket.getPitch(666));
//        }
    }

    public static void onAfterPacket(Packet<?> packet) {
    }
}
