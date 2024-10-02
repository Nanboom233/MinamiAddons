package top.nanboom233.Mixins;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import top.nanboom233.MinamiAddons;
import top.nanboom233.Tests.DamageOverlay;

import static top.nanboom233.MinamiAddons.mc;

/**
 * @author Nanboom233
 * @since 2024/10/2
 */
@Mixin(PlayerEntity.class)
public class MixinPlayerEntity {
    @Inject(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;increaseStat(Lnet/minecraft/util/Identifier;I)V"
            ),
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void trackDamageDealt(Entity target, CallbackInfo ci, float f, ItemStack itemStack, DamageSource damageSource, float g, float h, boolean bl, boolean bl2, boolean bl3, float i, boolean bl4, double d, float j, boolean bl5, float k, Entity entity, boolean bl6, float n) {
        if (!MinamiAddons.config.enableDamageOverlay) {
            return;
        }
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player.equals(mc.player)) {
            DamageOverlay.damageDealtCallback(i, n, bl3);
        }
    }

    @Inject(
            method = "damage",
            at = @At("RETURN")
    )
    private void trackDamageTaken(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!MinamiAddons.config.enableDamageOverlay) {
            return;
        }
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player.equals(mc.player)) {
            DamageOverlay.damageTakenCallback(source, amount);
        }
    }
}
