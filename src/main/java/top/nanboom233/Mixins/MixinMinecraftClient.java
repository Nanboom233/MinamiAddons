package top.nanboom233.Mixins;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.nanboom233.Config.Keybind.MultiKeybind;
import top.nanboom233.Handlers.TickHandler;


@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {
    @Inject(
            method = "run",
            at = @At("HEAD")
    )
    private void init(CallbackInfo info) {
    }

    @Inject(
            method = "tick()V",
            at = @At("RETURN")
    )
    private void onTick(CallbackInfo ci) {
        MultiKeybind.onKeyRelease();
        TickHandler.getInstance().onClientEndTick((MinecraftClient) (Object) this);
    }
}