package top.nanboom233.Mixins;

import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.nanboom233.MinamiAddons;
import top.nanboom233.Utils.Handlers.Events.KeyInputEvent;

@Mixin(Keyboard.class)
public class MixinKeyboard {
    @Inject(
            method = "onKey",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/Keyboard;debugCrashStartTime:J",
                    ordinal = 0
            )
    )
    private void onKeyboardInput(long windowPointer, int key, int scanCode, int action, int modifiers, CallbackInfo ci) {
        MinamiAddons.eventBus.post(new KeyInputEvent(windowPointer, key, scanCode, action != 0));
    }
}