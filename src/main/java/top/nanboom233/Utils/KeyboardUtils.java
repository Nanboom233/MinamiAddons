package top.nanboom233.Utils;

import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import top.nanboom233.Config.Keybind.KeyCodes;

public class KeyboardUtils {
    public static boolean isKeyDown(int keyCode) {
        long window = MinecraftClient.getInstance().getWindow().getHandle();

        return (keyCode >= 0 && GLFW.glfwGetKey(window, keyCode) == GLFW.GLFW_PRESS) ||
                (keyCode <= -100 && GLFW.glfwGetMouseButton(window, keyCode) == GLFW.GLFW_PRESS);
    }

    @Nullable
    public static String getStorageStringForKeyCode(int keyCode) {
        return KeyCodes.getNameForKey(keyCode);
    }
}
