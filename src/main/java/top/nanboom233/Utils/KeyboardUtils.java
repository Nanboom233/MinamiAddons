package top.nanboom233.Utils;

import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import top.nanboom233.Config.Keybind.KeyCodes;
import top.nanboom233.Config.Keybind.MultiKeybind;
import top.nanboom233.Utils.Handlers.Events.KeyInputEvent;
import top.nanboom233.Utils.Handlers.Events.TickEndEvent;
import top.nanboom233.Utils.Handlers.SubscribeEvent;
import top.nanboom233.Utils.Texts.ChatUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static top.nanboom233.Config.Keybind.KeyCodes.KEY_NONE;
import static top.nanboom233.MinamiAddons.config;

public class KeyboardUtils {
    private static final HashSet<Integer> pressedKeys = new HashSet<>();
    private static final ArrayList<MultiKeybind> keybindList = new ArrayList<>();

    public static Set<Integer> getPressedKeys() {
        return pressedKeys;
    }

    public static String getActiveKeysString() {
        if (!pressedKeys.isEmpty()) {
            StringBuilder sb = new StringBuilder(128);
            int i = 0;

            for (int key : pressedKeys) {
                if (i > 0) {
                    sb.append(" + ");
                }

                String name = getStorageStringForKeyCode(key);

                if (name != null) {
                    sb.append(String.format("%s (%d)", name, key));
                }

                i++;
            }

            return sb.toString();
        }

        return "<none>";
    }

    private static void printDebugMessage(int keyCode, int scanCode, boolean keyState) {
        String keyName = keyCode != KEY_NONE ? KeyCodes.getNameForKey(keyCode) : "<unknown>";
        String type = keyState ? "PRESS" : "RELEASE";
        String held = getActiveKeysString();
        String msg = String.format("%s %s (%d), held keys: %s", type, keyName, keyCode, held);
        ChatUtils.printActionbarMessage(msg);
    }

    public static void registerKeybind(MultiKeybind keybind) {
        keybindList.add(keybind);
    }

    @SubscribeEvent
    public static void updateKeyboard(TickEndEvent event) {
        //update every keybind's state
        for (MultiKeybind keybind : keybindList) {
            keybind.updateState();
        }

        //remove keys that have already been released
        pressedKeys.removeIf(integer -> !isKeyDown(integer));
    }

    public static boolean isKeyDown(int keyCode) {
        long window = MinecraftClient.getInstance().getWindow().getHandle();

        return (keyCode >= 0 && GLFW.glfwGetKey(window, keyCode) == GLFW.GLFW_PRESS) ||
                (keyCode <= -100 && GLFW.glfwGetMouseButton(window, keyCode) == GLFW.GLFW_PRESS);
    }

    @SubscribeEvent
    public static void onKeyInput(KeyInputEvent event) {
        if (event.state) {
            pressedKeys.add(event.keyCode);
        } else {
            pressedKeys.remove(event.keyCode);
        }
        if (config.printKeyboardDebugMessage) {
            printDebugMessage(event.keyCode, event.scanCode, event.state);
        }
    }

    @Nullable
    public static String getStorageStringForKeyCode(int keyCode) {
        return KeyCodes.getNameForKey(keyCode);
    }
}
