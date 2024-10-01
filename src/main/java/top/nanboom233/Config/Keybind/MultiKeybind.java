package top.nanboom233.Config.Keybind;

import top.nanboom233.MinamiAddons;
import top.nanboom233.Utils.KeyboardUtils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static top.nanboom233.Config.Keybind.KeyCodes.KEY_NONE;
import static top.nanboom233.Config.Keybind.MultiKeybind.scopeType.INGAME;
import static top.nanboom233.Utils.KeyboardUtils.getPressedKeys;

public class MultiKeybind {
    public final scopeType scope;
    public KeyActionType action;
    public final boolean allowExtraKeys;
    private HashSet<Integer> keyCodes;
    private boolean pressed;
    private boolean lastPressed = false;
    private long holdTicks = 0;

    public MultiKeybind(Set<Integer> keyCodes, scopeType type, KeyActionType action, boolean allowExtraKeys) {
        this.keyCodes = new HashSet<>(keyCodes);
        this.scope = type;
        this.action = action;
        this.allowExtraKeys = allowExtraKeys;
        KeyboardUtils.registerKeybind(this);
    }

    public boolean triggered() {
        boolean onPress = pressed && holdTicks == 1;
        boolean onRelease = !pressed && lastPressed;
        return switch (action) {
            case ANY -> onPress || onRelease;
            case PRESS -> onPress;
            case RELEASE -> onRelease;
        };
    }

    public void updateState() {
        if (keyCodes.isEmpty() ||
                (this.scope == INGAME) != (MinamiAddons.getCurrentScreen() == null)) {
            pressed = false;
        } else {
            final int sizePressed = getPressedKeys().size();
            final int sizeRequired = this.keyCodes.size();

            if (sizePressed == sizeRequired || (allowExtraKeys && sizePressed > sizeRequired)) {
                pressed = getPressedKeys().containsAll(this.keyCodes);
            } else {
                pressed = false;
            }
        }

        if (pressed) {
            holdTicks++;
        } else {
            holdTicks = 0;
        }
        lastPressed = pressed;
    }

    public boolean isBeingHeld() {
        return pressed;
    }

    public HashSet<Integer> getKeyCodes() {
        return keyCodes;
    }

    public void set(Set<Integer> keyCodes) {
        this.keyCodes = new HashSet<>(keyCodes);
    }

    public boolean matches(Set<Integer> keyCodes) {
        if (allowExtraKeys) {
            return keyCodes.containsAll(this.keyCodes);
        }
        return this.keyCodes.equals(keyCodes);
    }

    public boolean matches(int keyCode) {
        return this.keyCodes.equals(Set.of(keyCode));
    }

    public boolean isNone() {
        return this.matches(KEY_NONE);
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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MultiKeybind m)) {
            return false;
        }
        return m.scope == this.scope &&
                m.action == this.action &&
                m.keyCodes.equals(this.keyCodes) &&
                m.allowExtraKeys == this.allowExtraKeys;
    }

    public enum scopeType {
        INGAME, GUI, ANY
    }

    public enum KeyActionType {
        PRESS, RELEASE, ANY
    }
}