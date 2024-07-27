package top.nanboom233.Config;

import top.nanboom233.Commands.MinamiCommandManager;
import top.nanboom233.Config.Keybind.KeyCodes;
import top.nanboom233.MinamiAddons;
import top.nanboom233.Module.ModuleManager;
import top.nanboom233.Module.ModuleTemplate;
import top.nanboom233.Utils.Texts.ChatUtils;
import top.nanboom233.Utils.Texts.MinamiStyles;
import top.nanboom233.Utils.Texts.MinamiTextComponent;

import java.io.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import static top.nanboom233.Config.Keybind.KeyCodes.KEY_NONE;
import static top.nanboom233.MinamiAddons.config;
import static top.nanboom233.Utils.Texts.MinamiStyles.*;

public class KeybindConfig {
    private static final KeybindConfig INSTANCE = new KeybindConfig();
    private final Properties prop;

    public KeybindConfig() {
        prop = new Properties();
        loadConfig();
    }

    private void loadConfig() {
        //load keybinds
        InputStream input = null;
        try {
            File file = new File(Config.CONFIG_PATH + "keybinds.properties");
            if (!file.exists()) {
                file.createNewFile();
            }
            input = new FileInputStream(Config.CONFIG_PATH + "keybinds.properties");
            prop.load(input);
            Set<String> keys = prop.stringPropertyNames();
            for (String key : keys) {
                long value = Long.parseLong(prop.getProperty(key));
                if (!setKeybind(key, parseToSet(value))) {
                    MinamiAddons.getLogger().fatal("Failed to set keybind for " + key + " as " + parseToSet(value));
                }
            }

        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean writeConfig(String featureName, long keyCodesLong) {
        OutputStream output = null;
        try {
            output = new FileOutputStream(Config.CONFIG_PATH + "keybinds.properties");
            prop.setProperty(featureName, String.valueOf(keyCodesLong));
            prop.store(output, null);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    public static KeybindConfig getInstance() {
        return INSTANCE;
    }

    public static void colorFeatureName(String featureName, MinamiTextComponent text) {
        if ("Config".equals(featureName)) {
            text.lastComponent.withStyles(GOLD, BOLD);
        } else {
            text.lastComponent.withStyles(AQUA, BOLD);
        }
    }

    public static MinamiTextComponent getKeyInfo(Set<String> keys, MinamiStyles... styles) {
        MinamiTextComponent keyInfo = new MinamiTextComponent("");
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            keyInfo.append(new MinamiTextComponent(iterator.next()).withStyles(styles));
            if (iterator.hasNext()) {
                keyInfo.append(new MinamiTextComponent(" + "));
            }
        }
        return keyInfo;
    }

    public static int commandSetKeybind(String featureName, long keyCodesLong) {
        Set<Integer> keyCodes = parseToSet(keyCodesLong);
        Set<String> keyBindStrings =
                keyCodes.stream()
                        .map(KeyCodes::getNameForKey)
                        .collect(Collectors.toSet());
        MinamiCommandManager.beforeRender();
        if (getInstance().setKeybind(featureName, keyCodes)
                && getInstance().writeConfig(featureName, keyCodesLong)) {
            showSuccessMessage(featureName, keyBindStrings);
            return 1;
        }
        MinamiAddons.getLogger().fatal("Failed to set keybind for " + featureName + " as " + keyBindStrings);
        ChatUtils.debug("failed setting keybinds.", ChatUtils.MessageCategory.ERROR);
        return 0;
    }

    public boolean setKeybind(String featureName, Set<Integer> keyCodes) {
        //check keyCodes is vaild.
        for (int keyCode : keyCodes) {
            if (KeyCodes.getNameForKey(keyCode) == null) {
                return false;
            }
        }

        if ("Config".equals(featureName)) {
            config.keybind.setKeybind(keyCodes);
            return true;
        } else {
            for (ModuleTemplate module : ModuleManager.getInstance().getModuleList()) {
                if (featureName.equals(module.moduleName)) {
                    module.keybind.setKeybind(keyCodes);
                    return true;
                }
            }
        }
        return false;
    }

    public static long parseToLong(Set<Integer> keyCodes) {
        if (keyCodes.equals(Set.of(KEY_NONE))) {
            return 0;
        }
        long keyCodesLong = 0;
        for (int keyCode : keyCodes) {
            keyCodesLong *= 1000;
            keyCodesLong += keyCode;
        }
        return keyCodesLong;
    }

    public static Set<Integer> parseToSet(long keyCodesLong) {
        Set<Integer> keyCodes = new HashSet<>();
        if (keyCodesLong == 0) {
            keyCodes.add(KEY_NONE);
        }
        while (keyCodesLong > 0) {
            int keyCode = (int) (keyCodesLong % 1000);
            keyCodes.add(keyCode);
            keyCodesLong /= 1000;
        }
        return keyCodes;
    }

    public static void showSuccessMessage(String featureName, Set<String> keyBindStrings) {
        MinamiTextComponent successMessage = new MinamiTextComponent("Keybind for ")
                .append(new MinamiTextComponent(featureName));
        colorFeatureName(featureName, successMessage);
        successMessage.append(new MinamiTextComponent(" is set to :"));
        ChatUtils.show(new MinamiTextComponent("___________________________________").withStyles(GRAY));
        ChatUtils.emptyLine();
        ChatUtils.show(successMessage);
        ChatUtils.show(getKeyInfo(keyBindStrings, BOLD));
        ChatUtils.emptyLine();
        ChatUtils.show(new MinamiTextComponent("-----------------------------------").withStyles(GRAY));
    }
}
