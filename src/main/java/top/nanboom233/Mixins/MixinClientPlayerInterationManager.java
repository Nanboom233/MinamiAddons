package top.nanboom233.Mixins;

import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.nanboom233.Features.Player.AttackSwitcher;
import top.nanboom233.MinamiAddons;
import top.nanboom233.Utils.Handlers.Events.PlayerAttackEvent;

/**
 * @author Nanboom233
 * @since 2024/10/2
 */
@Mixin(ClientPlayerInteractionManager.class)
public class MixinClientPlayerInterationManager {

    @Inject(
            method = "attackEntity",
            at = @At("HEAD")
    )
    private void preAttack(PlayerEntity player, Entity target, CallbackInfo ci) {
        MinamiAddons.eventBus.post(new PlayerAttackEvent(target));
    }

    @Inject(
            method = "attackEntity",
            at = @At("RETURN")
    )
    private void postAttack(PlayerEntity player, Entity target, CallbackInfo ci) {
        AttackSwitcher.recoverSlot();
    }
}
