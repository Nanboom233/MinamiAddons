package top.nanboom233.Utils.Keybind;

import top.nanboom233.Handlers.TickHandler;
import top.nanboom233.MinamiAddons;
import top.nanboom233.Utils.InfoUtils;
import top.nanboom233.Utils.KeyboardUtils;

import java.util.HashSet;
import java.util.Set;

public class MultiKeybind implements IKeybindCategory {
    private static final HashSet<Integer> pressedKeys = new HashSet<>();
    private HashSet<Integer> keyCodes;
    private AdvancedKeybind keybind;
    private boolean pressed;
    private boolean lastPressed;
    private long holdTicks = 0;

    public final TickHandler.ITickTask updatePressed = (mc -> {
        updateIsPressed();
        tick();
    });

    private static boolean debugMode = false;

    public static void setDebugMode(boolean debugMode) {
        MultiKeybind.debugMode = debugMode;
    }

    public MultiKeybind(AdvancedKeybind keybind) {
        setKeybind(keybind);
        TickHandler.getInstance().register(updatePressed);
    }

    @Override
    public boolean wasPressed() {
        return pressed && holdTicks == 1;
    }

    @Override
    public void tick() {
        if (pressed) {
            holdTicks++;
        } else {
            holdTicks = 0;
        }
    }

    @Override
    public boolean isBeingHeld() {
        return pressed;
    }

    @Override
    public AdvancedKeybind getKeybind() {
        return keybind;
    }

    @Override
    public void setKeybind(AdvancedKeybind keybind) {
        this.keybind = keybind;
        keyCodes = new HashSet<>(keybind.bindKeys);
    }

    @Override
    public boolean matches(Set<Integer> keyCodes) {
        if (keybind.allowExtraKeys) {
            return keyCodes.containsAll(keybind.bindKeys);
        }
        return keybind.bindKeys.equals(keyCodes);
    }

    @Override
    public boolean matches(int keyCode) {
        if (keybind.allowExtraKeys) {
            return keybind.bindKeys.contains(keyCode);
        }
        return keybind.bindKeys.equals(Set.of(keyCode));
    }

    @Override
    public boolean triggerKeyAction() {
        boolean result = false;
//        if (!pressed) {
//            holdTicks = 0;
//            if (lastPressed && callback != null && (keybind.action == KeyAction.ANY ||
//                    keybind.action == KeyAction.RELEASE)) {
//                result = callback.onKeyAction(KeyAction.RELEASE, this);
//            }
//        } else if (!lastPressed && holdTicks == 0) {
//            if (callback != null && (keybind.action == KeyAction.PRESS || keybind.action == KeyAction.ANY)) {
//                result = callback.onKeyAction(KeyAction.PRESS, this);
//            }
//        }
        return result;
    }

    @Override
    public void updateIsPressed() {
        if (keyCodes.isEmpty() ||
                (this.keybind.type == AdvancedKeybind.KeyTriggerType.INGAME) != (MinamiAddons.getCurrentScreen() == null)) {
            pressed = false;
            return;
        }

        final int sizePressed = pressedKeys.size();
        final int sizeRequired = this.keyCodes.size();

        if (sizePressed == sizeRequired || (keybind.allowExtraKeys && sizePressed > sizeRequired)) {
            this.pressed = pressedKeys.containsAll(this.keyCodes);
        } else {
            pressed = false;
        }
    }

    public static void reCheckPressedKeys() {
        pressedKeys.removeIf(integer -> !KeyboardUtils.isKeyDown(integer));
    }

    public static void onKeyInput(int keyCode, int scanCode, boolean state) {
        if (state) {
            pressedKeys.add(keyCode);
        } else {
            pressedKeys.remove(keyCode);
        }
        if (debugMode) {
            printKeybindDebugMessage(keyCode, scanCode, state);
        }
    }

    private static void printKeybindDebugMessage(int keyCode, int scanCode, boolean keyState) {
        String keyName = keyCode != KeyCodes.KEY_NONE ? KeyCodes.getNameForKey(keyCode) : "<unknown>";
        String type = keyState ? "PRESS" : "RELEASE";
        String held = getActiveKeysString();
        String msg = String.format("%s %s (%d), held keys: %s", type, keyName, keyCode, held);

        if (MinamiAddons.config.printKeyboardDebugMessage) {
            InfoUtils.printActionbarMessage(msg);
        }
    }

    public static String getActiveKeysString() {
        if (!pressedKeys.isEmpty()) {
            StringBuilder sb = new StringBuilder(128);
            int i = 0;

            for (int key : pressedKeys) {
                if (i > 0) {
                    sb.append(" + ");
                }

                String name = KeyboardUtils.getStorageStringForKeyCode(key);

                if (name != null) {
                    sb.append(String.format("%s (%d)", name, key));
                }

                i++;
            }

            return sb.toString();
        }

        return "<none>";
    }

}