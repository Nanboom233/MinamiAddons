package top.nanboom233.Config;


import gg.essential.universal.UScreen;
import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.Property;
import gg.essential.vigilance.data.PropertyType;
import net.minecraft.client.gui.hud.MessageIndicator;
import top.nanboom233.Handlers.TickHandler;
import top.nanboom233.Utils.InfoUtils;
import top.nanboom233.Utils.Keybind.AdvancedKeybind;
import top.nanboom233.Utils.Keybind.KeyCodes;
import top.nanboom233.Utils.Keybind.MultiKeybind;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static top.nanboom233.Utils.Keybind.AdvancedKeybind.KeyTriggerType.INGAME;

public class Config extends Vigilant {
    private static final String CONFIG_FILE_PATH = "./config/MimamiAddons.toml";
    private static final Config INSTANCE = new Config();
    private static final MultiKeybind keyBinding = new MultiKeybind(
            new AdvancedKeybind(INGAME, false, new HashSet<>(Set.of(KeyCodes.KEY_L))));

    public static Config getInstance() {
        return INSTANCE;
    }

    public static List<MessageIndicator> timestampWhitelist = new ArrayList<>(List.of(InfoUtils.minamiIndicater));

    public final TickHandler.ITickTask configOpenHandle = mc -> {
        if (keyBinding.wasPressed()) {
            if (mc.currentScreen == null) {
//                mc.setScreen(Config.getInstance().gui());
                UScreen.displayScreen(Config.getInstance().gui());
            }
        }
    };


    public Config() {
        super(new File(CONFIG_FILE_PATH), "MinamiAddons");
        initialize();
        TickHandler.getInstance().register(configOpenHandle);
    }

    @Property(type = PropertyType.CHECKBOX,
            name = "Print Keyboard Message",
            description = "Print keyboard debug message in the action bar.",
            category = "Debugs"
    )
    public boolean printKeyboardDebugMessage = false;

    @Property(type = PropertyType.CHECKBOX,
            name = "Add Timestamp to Chat",
            description = "Add a gray timestamp to chats.",
            category = "Chats"
    )
    public boolean chatTimestamp = false;


    @Property(type = PropertyType.CHECKBOX,
            name = "Chat Copy",
            description = "Copy chat messages by right-clicking; Hold Shift and right-click to copy messages with formatting.",
            category = "Chats"
    )
    public boolean chatCopy = false;

    @Property(type = PropertyType.CHECKBOX,
            name = "Ghost Block",
            description = "Make ghost blocks with keybinds.",
            category = "World"
    )
    public boolean ghostBlock = false;

}

