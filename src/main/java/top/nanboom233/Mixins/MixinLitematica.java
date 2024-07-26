package top.nanboom233.Mixins;

import fi.dy.masa.litematica.util.WorldUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.nanboom233.Features.World.EasyPlaceFix;

import static top.nanboom233.MinamiAddons.config;

@Mixin(WorldUtils.class)
public class MixinLitematica {
    @Inject(
            method = "applyBlockSlabProtocol",
            at = @At("HEAD")
    )
    private static void fixMovePacket(BlockPos pos, BlockState state, Vec3d hitVecIn, CallbackInfoReturnable<Vec3d> cir) {
        if (!config.easyPlaceFix) {
            return;
        }
        EasyPlaceFix.beforePlacement(pos, state, hitVecIn);
    }

    @Inject(
            method = "doEasyPlaceAction",
            at = @At(
                    value = "RETURN",
                    ordinal = 8
            )
    )
    private static void restoreMovePacketAndAdjustment(MinecraftClient mc, CallbackInfoReturnable<Vec3d> cir) {
        if (!config.easyPlaceFix) {
            return;
        }
        EasyPlaceFix.afterPlacement();
    }

    @Inject(
            method = "applyPlacementFacing",
            at = @At(
                    value = "RETURN",
                    ordinal = 3
            ),
            cancellable = true
    )
    private static void directionFix(BlockState stateSchematic, Direction side, BlockState stateClient, CallbackInfoReturnable<Direction> cir) {
        if (!config.easyPlaceFix) {
            return;
        }
        Direction newFacing = EasyPlaceFix.directionFix(stateSchematic, side);
        if (newFacing != null) {
            cir.setReturnValue(newFacing);
        }
    }
}
