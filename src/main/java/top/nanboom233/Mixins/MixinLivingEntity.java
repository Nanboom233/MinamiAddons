package top.nanboom233.Mixins;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author Nanboom233
 * @since 2024/10/2
 */
@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {
//
//    @Unique
//    private float previousHealth = 0.0F;
//
//    @Inject(
//            method = "damage",
//            at = @At("TAIL")
//    )
//    private void healthTracker(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
//        LivingEntity entity = (LivingEntity) (Object) this;
//
//        World world = entity.getWorld();
//        if (world == null || !world.isClient()) {
//            return;
//        }
//
//        if (source.getAttacker() instanceof PlayerEntity) {
//            PlayerEntity player = (PlayerEntity) source.getAttacker();
//            if (player.equals(mc.player)) {
//
//            }
//        }
//
//        float currentHealth = entity.getHealth();
//
//        if (previousHealth != currentHealth) {
//            previousHealth = currentHealth;
//        }
//    }
}
