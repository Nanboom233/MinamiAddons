package top.nanboom233.Utils;

import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.util.Pair;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import top.nanboom233.MinamiAddons;

import static top.nanboom233.MinamiAddons.mc;

public class ControlUtils {
    @Nullable
    public static Pair<Float, Float> getFaceYawAndPitch(float tx, float ty, float tz) {
        if (mc.player == null) {
            return null;
        }

        System.err.printf("facing %.2f %.2f %.2f%n", tx, ty, tz);
        float PI = (float) Math.PI;
        float x = (float) mc.player.getX(),
                y = (float) mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()),
                z = (float) mc.player.getZ();
        float dx = tx - x, dy = ty - y, dz = tz - z;
        float X = (float) Math.sqrt((x - tx) * (x - tx) + (z - tz) * (z - tz));
        float alpha = (float) Math.atan2(dy, X);

        float pitch = -180 / PI * alpha;
        float yaw = (float) (Math.atan2(dz, dx) + 3F * PI / 2F);
        while (yaw < -PI) {
            yaw += 2 * PI;
        }
        while (yaw > PI) {
            yaw -= 2 * PI;
        }
        yaw = 180 / PI * yaw;
        return new Pair<>(yaw, pitch);
    }

    public static Pair<Float, Float> getFaceYawAndPitch(Vec3d vec) {
        return getFaceYawAndPitch((float) vec.getX(), (float) vec.getY(), (float) vec.getZ());
    }

    public static void setSlot(int slot) {
        if (mc.player == null || mc.world == null) {
            return;
        }

        try {
            mc.player.getInventory().setSelectedSlot(slot);
        } catch (IllegalArgumentException e) {
            MinamiAddons.logger.error("Attempt to switch to a invaild slot: " + slot);
        }
    }

    public static int getCurrentSlot() {
        if (mc.player == null || mc.world == null) {
            return -1;
        }

        return mc.player.getInventory().getSelectedSlot();
    }

    public static void setSneaking(boolean sneak) {
        if (mc.player == null || mc.world == null || mc.getNetworkHandler() == null) {
            return;
        }

        if (mc.player.isSneaking() != sneak) {
            PlayerInput originalInput = mc.player.input.playerInput;
            mc.player.setSneaking(sneak);
            mc.getNetworkHandler().sendPacket(new PlayerInputC2SPacket(
                    new PlayerInput(
                            originalInput.forward(),
                            originalInput.backward(),
                            originalInput.left(),
                            originalInput.right(),
                            originalInput.jump(),
                            sneak,
                            originalInput.sprint()
                    )
            ));
        }
    }
}
