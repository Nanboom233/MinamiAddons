package top.nanboom233.Handlers;

import top.nanboom233.Utils.Keybind.MultiKeybind;

public class KeyboardInputHandler {
    public static void onKeyInput(int keyCode, int scanCode, int modifiers, boolean eventKeyState) {
        MultiKeybind.onKeyInput(keyCode, scanCode, eventKeyState);
    }
}