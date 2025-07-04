package top.nanboom233.Config;


import gg.essential.universal.UScreen;
import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.Property;
import gg.essential.vigilance.data.PropertyType;
import net.minecraft.client.gui.hud.MessageIndicator;
import top.nanboom233.Commands.KeybindCommand;
import top.nanboom233.Commands.MinamiCommandManager;
import top.nanboom233.Config.Keybind.MultiKeybind;
import top.nanboom233.Handlers.TickHandler;
import top.nanboom233.Utils.Texts.ChatUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static top.nanboom233.Config.Keybind.KeyCodes.KEY_NONE;
import static top.nanboom233.Config.Keybind.MultiKeybind.KeyActionType.PRESS;
import static top.nanboom233.Config.Keybind.MultiKeybind.scopeType.INGAME;

public class Config extends Vigilant {
    public static final String CONFIG_PATH = "./config/MinamiAddons/";
    private static final Config INSTANCE = new Config();
    public final MultiKeybind keybind = new MultiKeybind(Set.of(KEY_NONE), INGAME, PRESS, false);

    public static Config getInstance() {
        return INSTANCE;
    }

    public static List<MessageIndicator> timestampWhitelist = new ArrayList<>(
            List.of(
                    ChatUtils.minamiMainIndicater,
                    MinamiCommandManager.minamiSettingsIndicater,
                    KeybindCommand.minamiKeybindSettingsIndicater
            ));

    public static List<MessageIndicator> copyWhiteList = new ArrayList<>(
            List.of(
                    ChatUtils.minamiMainIndicater,
                    MinamiCommandManager.minamiSettingsIndicater,
                    KeybindCommand.minamiKeybindSettingsIndicater
            ));

    public final TickHandler.ITickTask configOpenHandle = mc -> {
        if (keybind.isTriggered()) {
            if (mc.currentScreen == null) {
                UScreen.displayScreen(Config.getInstance().gui());
            }
        }
    };

    public Config() {
        super(new File(CONFIG_PATH + "Features.toml"), "MinamiAddons");
        initialize();
        TickHandler.getInstance().register(configOpenHandle);
    }

    @Property(
            type = PropertyType.CHECKBOX,
            name = "Print Keyboard Message",
            description = "Print keyboard debug message in the action bar.",
            category = "Debugs"
    )
    public boolean printKeyboardDebugMessage = false;

    @Property(
            type = PropertyType.CHECKBOX,
            name = "Add Timestamp to Chat",
            description = "Add a gray timestamp to chats.",
            category = "Chats"
    )
    public boolean chatTimestamp = false;


    @Property(
            type = PropertyType.CHECKBOX,
            name = "Chat Copy",
            description = "Copy chat messages by right-clicking; Hold Shift and right-click to copy messages with formatting.",
            category = "Chats"
    )
    public boolean chatCopy = false;

    @Property(
            type = PropertyType.CHECKBOX,
            name = "Ghost Block",
            description = "Make ghost blocks with keybinds.",
            category = "World"
    )
    public boolean ghostBlock = false;

    @Property(
            type = PropertyType.CHECKBOX,
            name = "Litematica EasyPlace Fix",
            description = "Fix EasyPlace to place blocks with states.",
            category = "World"
    )
    public boolean easyPlaceFix = false;

    @Property(
            type = PropertyType.NUMBER,
            min = 5,
            max = 60,
            name = "Minami Settings Timeout",
            description = "Adjust after how long the settings will disappear automaticly.",
            category = "Debugs"
    )
    public int settingsTimeout = 30;

}

