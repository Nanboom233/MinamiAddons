package top.nanboom233.Mixins;

import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.nanboom233.Handlers.KeyboardInputHandler;

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
        KeyboardInputHandler.onKeyInput(key, scanCode, modifiers, action != 0);
    }
}