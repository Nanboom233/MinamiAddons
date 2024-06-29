package top.nanboom233.Utils.Keybind;

import top.nanboom233.Config.IConfigOption;

import java.util.HashSet;

public class AdvancedKeybind {
    public final KeyTriggerType type;
    public final boolean allowExtraKeys;
    public HashSet<Integer> bindKeys;
    public KeyActionType action;

    public AdvancedKeybind(KeyTriggerType type, boolean allowExtraKeys, HashSet<Integer> bindKeys) {
        this.type = type;
        this.allowExtraKeys = allowExtraKeys;
        this.bindKeys = bindKeys;
    }

    public enum KeyTriggerType implements IConfigOption {
        INGAME("ingame", "pumpkin.keybindstemplate.type.ingame"),
        GUI("gui", "pumpkin.keybindstemplate.type.gui"),
        ANY("any", "pumpkin.keybindstemplate.type.any");

        private final String value;
        private final String translationKey;

        KeyTriggerType(String value, String translationKey) {
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
        public KeyTriggerType getFromValue(String value) {
            for (KeyTriggerType t : KeyTriggerType.values()) {
                if (t.value.equals(value)) {
                    return t;
                }
            }
            return null;
        }

        @Override
        public KeyTriggerType getFromTranslationKey(String translationKey) {
            for (KeyTriggerType t : KeyTriggerType.values()) {
                if (t.translationKey.equals(translationKey)) {
                    return t;
                }
            }
            return null;
        }
    }

    public enum KeyActionType implements IConfigOption {
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