package top.nanboom233.Mixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.screen.ChatScreen;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.nanboom233.Config.Keybind.KeyCodes;
import top.nanboom233.Features.Miscellaneous.ChatCopy;
import top.nanboom233.Utils.KeyboardUtils;

import static top.nanboom233.MinamiAddons.config;

@Mixin(Mouse.class)
public class MixinMouse {

    @Shadow
    private MinecraftClient client;

    @Inject(
            method = "onMouseButton",
            at = @At("RETURN")
    )
    private void onMouseButton(long window, int button, int action, int mods, CallbackInfo info) {
        if (!config.chatCopy) {
            return;
        }
        boolean bl = action == 1;
        if (window == this.client.getWindow().getHandle() &&
                this.client.currentScreen instanceof ChatScreen &&
                button == GLFW.GLFW_MOUSE_BUTTON_RIGHT && bl) {
            if (KeyboardUtils.isKeyDown(KeyCodes.KEY_LEFT_SHIFT) ||
                    KeyboardUtils.isKeyDown(KeyCodes.KEY_RIGHT_SHIFT)) {
                ChatCopy.styledCopy();
            } else {
                ChatCopy.regularCopy();
            }
        }
    }
}
