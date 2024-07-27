package top.nanboom233.Config.Keybind;

import top.nanboom233.Config.IEnumOption;
import top.nanboom233.Handlers.TickHandler;
import top.nanboom233.Handlers.TickHandler.ITickTask;
import top.nanboom233.MinamiAddons;
import top.nanboom233.Utils.KeyboardUtils;
import top.nanboom233.Utils.Texts.ChatUtils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static top.nanboom233.Config.Keybind.KeyCodes.KEY_NONE;
import static top.nanboom233.Config.Keybind.MultiKeybind.scopeType.INGAME;
import static top.nanboom233.MinamiAddons.config;

public class MultiKeybind implements IKeybindCategory {
    private static final HashSet<Integer> pressedKeys = new HashSet<>();
    public final scopeType scope;
    public KeyActionType action;
    public final boolean allowExtraKeys;
    private HashSet<Integer> keyCodes;
    private boolean pressed;
    private boolean lastPressed = false;
    private long holdTicks = 0;

    public final ITickTask updatePressed = (mc -> {
        updateIsPressed();
        tick();
    });

    public MultiKeybind(Set<Integer> keyCodes, scopeType type,
                        KeyActionType action, boolean allowExtraKeys) {
        this.keyCodes = new HashSet<>(keyCodes);
        this.scope = type;
        this.action = action;
        this.allowExtraKeys = allowExtraKeys;
        TickHandler.getInstance().register(updatePressed);
    }

    @Override
    public boolean isTriggered() {
        boolean onPress = pressed && holdTicks == 1;
        boolean onRelease = !pressed && lastPressed;
        return switch (action) {
            case ANY -> onPress || onRelease;
            case PRESS -> onPress;
            case RELEASE -> onRelease;
        };
    }

    @Override
    public void tick() {
        if (pressed) {
            holdTicks++;
        } else {
            holdTicks = 0;
        }
        lastPressed = pressed;
    }

    @Override
    public boolean isBeingHeld() {
        return pressed;
    }

    @Override
    public HashSet<Integer> getKeyCodes() {
        return keyCodes;
    }

    @Override
    public void setKeybind(Set<Integer> keyCodes) {
        this.keyCodes = new HashSet<>(keyCodes);
    }

    @Override
    public boolean matches(Set<Integer> keyCodes) {
        if (allowExtraKeys) {
            return keyCodes.containsAll(this.keyCodes);
        }
        return this.keyCodes.equals(keyCodes);
    }

    @Override
    public boolean matches(int keyCode) {
        return this.keyCodes.equals(Set.of(keyCode));
    }

    @Override
    public void updateIsPressed() {
        if (keyCodes.isEmpty() ||
                (this.scope == INGAME) != (MinamiAddons.getCurrentScreen() == null)) {
            pressed = false;
            return;
        }

        final int sizePressed = pressedKeys.size();
        final int sizeRequired = this.keyCodes.size();

        if (sizePressed == sizeRequired || (allowExtraKeys && sizePressed > sizeRequired)) {
            this.pressed = pressedKeys.containsAll(this.keyCodes);
        } else {
            pressed = false;
        }
    }

    @Override
    public boolean isNone() {
        return this.matches(KEY_NONE);
    }

    public static void onKeyRelease() {
        pressedKeys.removeIf(integer -> !KeyboardUtils.isKeyDown(integer));
    }

    public static void onKeyInput(int keyCode, int scanCode, boolean state) {
        if (state) {
            pressedKeys.add(keyCode);
        } else {
            pressedKeys.remove(keyCode);
        }
        if (config.printKeyboardDebugMessage) {
            printDebugMessage(keyCode, scanCode, state);
        }
    }

    private static void printDebugMessage(int keyCode, int scanCode, boolean keyState) {
        String keyName = keyCode != KEY_NONE ? KeyCodes.getNameForKey(keyCode) : "<unknown>";
        String type = keyState ? "PRESS" : "RELEASE";
        String held = getActiveKeysString();
        String msg = String.format("%s %s (%d), held keys: %s", type, keyName, keyCode, held);
        ChatUtils.printActionbarMessage(msg);
    }

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

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("[");
        Iterator<Integer> iterator = keyCodes.iterator();
        while (iterator.hasNext()) {
            stringBuilder.append(KeyCodes.getNameForKey(iterator.next()));
            if (iterator.hasNext()) {
                stringBuilder.append(" , ");
            }
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public enum scopeType implements IEnumOption {
        INGAME("ingame", "pumpkin.keybindstemplate.type.ingame"),
        GUI("gui", "pumpkin.keybindstemplate.type.gui"),
        ANY("any", "pumpkin.keybindstemplate.type.any");

        private final String value;
        private final String translationKey;

        scopeType(String value, String translationKey) {
            this.value = value;
            this.translationKey = translationKey;
        }

        @Override
        public String getStringValue() {
            return value;
        }

        @Override
        public String getTranslationKey() {
            return translationKey;
        }

        @Override
        public scopeType getFromValue(String value) {
            for (scopeType t : scopeType.values()) {
                if (t.value.equals(value)) {
                    return t;
                }
            }
            return null;
        }

        @Override
        public scopeType getFromTranslationKey(String translationKey) {
            for (scopeType t : scopeType.values()) {
                if (t.translationKey.equals(translationKey)) {
                    return t;
                }
            }
            return null;
        }
    }

    public enum KeyActionType implements IEnumOption {
        PRESS("press", "pumpkin.keybinds.keyaction.press"),
        RELEASE("release", "pumpkin.keybinds.keyaction.release"),
        ANY("any", "pumpkin.keybinds.keyaction.any");

        KeyActionType(String value, String translationKey) {
            this.value = value;
            this.translationKey = translationKey;
        }

        private final String value;

        private final String translationKey;

        @Override
        public String getStringValue() {
            return value;
        }

        @Override
        public String getTranslationKey() {
            return translationKey;
        }

        @Override
        public KeyActionType getFromValue(String value) {
            for (KeyActionType t : KeyActionType.values()) {
                if (t.value.equals(value)) {
                    return t;
                }
            }
            return null;
        }

        @Override
        public KeyActionType getFromTranslationKey(String translationKey) {
            for (KeyActionType t : KeyActionType.values()) {
                if (t.translationKey.equals(translationKey)) {
                    return t;
                }
            }
            return null;
        }
    }
}