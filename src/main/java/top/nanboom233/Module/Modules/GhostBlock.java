package top.nanboom233.Module.Modules;

import net.minecraft.block.Blocks;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import top.nanboom233.Module.ModuleTemplate;
import top.nanboom233.Utils.Keybind.AdvancedKeybind;
import top.nanboom233.Utils.Keybind.MultiKeybind;

import java.util.HashSet;
import java.util.Set;

import static net.minecraft.util.Hand.MAIN_HAND;
import static top.nanboom233.MinamiAddons.mc;
import static top.nanboom233.Utils.Keybind.AdvancedKeybind.KeyTriggerType.INGAME;

public class GhostBlock extends ModuleTemplate {
    public static final String moduleName = "GhostBlock";
    public static final String description = "Make ghost blocks with keybinds.";

    private static long lastSwing = -1;

    private static final MultiKeybind keyBinding = new MultiKeybind(
            new AdvancedKeybind(INGAME, true, new HashSet<>(Set.of(InputUtil.GLFW_KEY_X))));

    public GhostBlock() {
        super(moduleName, description, keyBinding);
    }

    @Override
    public void trigger() {
        super.trigger();
        if (mc.world != null && mc.player != null) {
            HitResult hitResult = mc.crosshairTarget;
            if (hitResult == null || hitResult.getType() != HitResult.Type.BLOCK) {
                return;
            }
            Vec3d vec3d = hitResult.getPos();
            BlockPos lookingAtPos = ((BlockHitResult) hitResult).getBlockPos();
//            InfoUtils.showInChat("vec3d:" + vec3d.toString(), InfoUtils.MessageCategory.DEBUG);
//            InfoUtils.showInChat("blockpos:" + lookingAtPos.toString(), InfoUtils.MessageCategory.DEBUG);
            long curTime = System.currentTimeMillis();
            if (lastSwing + 250 < curTime) {
                mc.player.swingHand(MAIN_HAND);
                lastSwing = curTime;
            }
            mc.world.setBlockState(lookingAtPos, Blocks.AIR.getDefaultState(), 3);
        }
    }
}
