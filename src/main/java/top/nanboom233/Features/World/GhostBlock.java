package top.nanboom233.Features.World;

import net.minecraft.block.Blocks;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import top.nanboom233.Config.Keybind.MultiKeybind;
import top.nanboom233.Utils.Module.ModuleTemplate;

import java.util.Set;

import static net.minecraft.util.Hand.MAIN_HAND;
import static top.nanboom233.Config.Keybind.KeyCodes.KEY_NONE;
import static top.nanboom233.Config.Keybind.MultiKeybind.KeyActionType.PRESS;
import static top.nanboom233.Config.Keybind.MultiKeybind.scopeType.INGAME;
import static top.nanboom233.MinamiAddons.config;
import static top.nanboom233.MinamiAddons.mc;

public class GhostBlock extends ModuleTemplate {
    public static final String moduleName = "GhostBlock";
    public static final String description = "Make ghost blocks with keybinds.";

    private static long lastSwing = -1;

    private static final MultiKeybind keyBinding = new MultiKeybind(Set.of(KEY_NONE), INGAME, PRESS, true);

    public GhostBlock() {
        super(moduleName, description, keyBinding);
    }

    @Override
    public void trigger() {
        if (!config.ghostBlock) {
            return;
        }
        if (mc.world != null && mc.player != null) {
            HitResult hitResult = mc.crosshairTarget;
            if (hitResult == null || hitResult.getType() != HitResult.Type.BLOCK) {
                return;
            }
            Vec3d vec3d = hitResult.getPos();
            BlockPos lookingAtPos = ((BlockHitResult) hitResult).getBlockPos();
            long curTime = System.currentTimeMillis();
            if (lastSwing + 250 < curTime) {
                mc.player.swingHand(MAIN_HAND);
                lastSwing = curTime;
            }
            mc.world.setBlockState(lookingAtPos, Blocks.AIR.getDefaultState(), 3);
        }
    }
}
