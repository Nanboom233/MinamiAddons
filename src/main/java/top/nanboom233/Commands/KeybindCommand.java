package top.nanboom233.Commands;

import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;
import top.nanboom233.Config.Config;
import top.nanboom233.Config.Keybind.KeyCodes;
import top.nanboom233.Config.Keybind.KeybindConfig;
import top.nanboom233.Config.Keybind.MultiKeybind;
import top.nanboom233.MinamiAddons;
import top.nanboom233.Mixins.MixinMinecraftClientAccessor;
import top.nanboom233.Utils.Handlers.Events.TickEndEvent;
import top.nanboom233.Utils.KeyboardUtils;
import top.nanboom233.Utils.Module.ModuleManager;
import top.nanboom233.Utils.Module.ModuleTemplate;
import top.nanboom233.Utils.Texts.ChatUtils;
import top.nanboom233.Utils.Texts.MinamiTextComponent;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.mojang.brigadier.arguments.LongArgumentType.getLong;
import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
import static net.minecraft.command.CommandSource.suggestMatching;
import static top.nanboom233.Config.Keybind.KeyCodes.KEY_ESCAPE;
import static top.nanboom233.Config.Keybind.KeyCodes.KEY_NONE;
import static top.nanboom233.Config.Keybind.KeybindConfig.commandSetKeybind;
import static top.nanboom233.Config.Keybind.KeybindConfig.parseToLong;
import static top.nanboom233.MinamiAddons.config;
import static top.nanboom233.MinamiAddons.mc;
import static top.nanboom233.Utils.Texts.MinamiStyles.*;

public class KeybindCommand {
    public static LiteralArgumentBuilder<FabricClientCommandSource> register(LiteralArgumentBuilder<FabricClientCommandSource> literalargumentbuilder,
                                                                             CommandRegistryAccess commandRegistryAccess) {
        return literalargumentbuilder
                .then(literal("keybind").executes((source) -> KeybindCommand.listKeybinds())
                        .then(argument("keybindable features", StringArgumentType.word())
                                .suggests((context, builder) -> suggestMatching(getKeybindableFeatures(), builder))
                                .executes((context) -> keybindGuide(getString(context, "keybindable features")))
                                .then(argument("keyCodes", LongArgumentType.longArg())
                                        .executes((context -> commandSetKeybind(getString(context, "keybindable features"), getLong(context, "keyCodes")))))));
    }

    private static final Text description = new MinamiTextComponent("MinamiAddons").addStyles(BOLD, GOLD)
            .append(new MinamiTextComponent("'s Keybinds Settings Interface")).getText();
    public static final MessageIndicator minamiKeybindSettingsIndicater = new MessageIndicator(
            727727, null,
            description, "MinamiAddons");
    private static Set<String> keybindableFeatures = null;
    public static Consumer<TickEndEvent> setKeybindProgress;
    public static Set<Integer> aleardyPressedKeyCodes;
    public static boolean hasInput;
    public static boolean firstShown;
    public static long settingStartTime = -1;

    public static int listKeybinds() {
        MinamiCommandManager.beforeRender();
        show(new MinamiTextComponent("___________________________________").addStyles(GRAY));
        emptyLine();
        show(new MinamiTextComponent("Minami Keybinds Settings:").addStyles(GRAY));

        //config keybind
        MinamiTextComponent configKeybindInfo = getKeybindInfoText("Config", Config.keybind);
        show(configKeybindInfo);

        //modules keybinds
        for (ModuleTemplate module : ModuleManager.getInstance().getModuleList()) {
            MinamiTextComponent modulesKeybindInfo = getKeybindInfoText(module.moduleName, module.keybind);
            show(modulesKeybindInfo);
        }

        emptyLine();
        show(new MinamiTextComponent("-----------------------------------").addStyles(GRAY));
        return 1;
    }

    public static MinamiTextComponent getKeybindInfoText(String name, MultiKeybind keybind) {
        MinamiTextComponent keybindInfo = new MinamiTextComponent().append(
                new MinamiTextComponent(" - " + name + ": "));
        KeybindConfig.colorFeatureName(name, keybindInfo);
        if (keybind.isNone()) {
            keybindInfo.append(new MinamiTextComponent("[NONE]").addStyles(YELLOW)
                    .showText(new MinamiTextComponent("Click to set keybind for " + name + ".").addStyles(YELLOW))
                    .runCommand("/minami keybind " + name)
            );
        } else {
            keybindInfo.append(new MinamiTextComponent(keybind.toString())
                    .showText(new MinamiTextComponent("Click to change keybind for " + name + ".").addStyles(GRAY))
                    .runCommand("/minami keybind " + name)
            );
        }
        return keybindInfo;
    }

    public static void show(MinamiTextComponent text) {
        ChatUtils.show(text, minamiKeybindSettingsIndicater);
    }

    public static void emptyLine() {
        ChatUtils.emptyLine(minamiKeybindSettingsIndicater);
    }

    public static Set<String> getKeybindableFeatures() {
        if (keybindableFeatures != null) {
            return keybindableFeatures;
        }
        keybindableFeatures = new HashSet<>();
        keybindableFeatures.add("Config");
        for (ModuleTemplate module : ModuleManager.getInstance().getModuleList()) {
            keybindableFeatures.add(module.moduleName);
        }
        return keybindableFeatures;
    }

    public static int keybindGuide(String keybindableFeature) {
        if (mc.currentScreen == null) {
            mc.execute(() ->
                    ((MixinMinecraftClientAccessor) mc).invokeOpenChatScreen("")
            );
        }
        aleardyPressedKeyCodes = new HashSet<>();
        hasInput = false;
        firstShown = false;
        setKeybindProgress = (TickEndEvent event) -> {
            if (System.currentTimeMillis() - settingStartTime < 200) {
                return;
            }
            Set<Integer> pressedKeyCodes = new HashSet<>(KeyboardUtils.getPressedKeys());
            Set<String> pressedKeys =
                    pressedKeyCodes.stream()
                            .map(KeyCodes::getNameForKey)
                            .collect(Collectors.toSet());
            if (!aleardyPressedKeyCodes.containsAll(pressedKeyCodes) || !firstShown) {
                updateKeybindRender(keybindableFeature, pressedKeys);
                firstShown = true;
            }
            aleardyPressedKeyCodes.addAll(pressedKeyCodes);
            if (!pressedKeyCodes.isEmpty()) {
                hasInput = true;
            } else if (hasInput) {
                if (aleardyPressedKeyCodes.equals(Set.of(KEY_ESCAPE))) {
                    aleardyPressedKeyCodes = Set.of(KEY_NONE);
                }
                Set<String> alreadyPressedKeys =
                        aleardyPressedKeyCodes.stream()
                                .map(KeyCodes::getNameForKey)
                                .collect(Collectors.toSet());
                MinamiCommandManager.beforeRender();
                MinamiTextComponent confirmInfo = new MinamiTextComponent("Keybind for ")
                        .append(new MinamiTextComponent(keybindableFeature));
                KeybindConfig.colorFeatureName(keybindableFeature, confirmInfo);
                confirmInfo.append(new MinamiTextComponent(" will be set to :"));
                show(new MinamiTextComponent("___________________________________").addStyles(GRAY));
                emptyLine();
                show(confirmInfo);
                emptyLine();
                show(getCenterAlignText(KeybindConfig.getKeyInfo(alreadyPressedKeys, BOLD, DARK_GREEN)));
                emptyLine();
                show(new MinamiTextComponent("[CONFIRM]").addStyles(DARK_GREEN, BOLD)
                        .showText(new MinamiTextComponent("Confirm and set the keybind.").addStyles(GRAY))
                        .runCommand("/minami keybind " + keybindableFeature + " " + parseToLong(aleardyPressedKeyCodes))
                        .append(new MinamiTextComponent("    "))
                        .append(new MinamiTextComponent("[RESET]").addStyles(RED, BOLD)
                                .showText(new MinamiTextComponent("Reset the keybind."))
                                .runCommand("/minami keybind " + keybindableFeature)));
                emptyLine();
                show(new MinamiTextComponent("-----------------------------------").addStyles(GRAY));
                MinamiAddons.eventBus.unregister(TickEndEvent.class, setKeybindProgress);
            }
            if (System.currentTimeMillis() - settingStartTime > config.settingsTimeout * 1000L) {
                MinamiCommandManager.beforeRender();
                ChatUtils.show(new MinamiTextComponent("Keybind Setting Aborted."));
                MinamiAddons.eventBus.unregister(TickEndEvent.class, setKeybindProgress);
            }
        };
        settingStartTime = System.currentTimeMillis();
        MinamiAddons.eventBus.register(TickEndEvent.class, setKeybindProgress);
        return 1;
    }

    public static void updateKeybindRender(String keybindableFeature, Set<String> pressedKeys) {
        MinamiCommandManager.beforeRender();
        show(new MinamiTextComponent("___________________________________").addStyles(GRAY));
        emptyLine();
        MinamiTextComponent setKeybindText = new MinamiTextComponent("Set Keybind for ")
                .append(new MinamiTextComponent(keybindableFeature + ":"));
        KeybindConfig.colorFeatureName(keybindableFeature, setKeybindText);
        show(setKeybindText);
        emptyLine();
        if (pressedKeys.isEmpty()) {
            show(getCenterAlignText(new MinamiTextComponent("[NONE]").addStyles(BOLD)));
        } else {
            show(getCenterAlignText(KeybindConfig.getKeyInfo(pressedKeys, BOLD, DARK_GREEN)));
        }
        emptyLine();
        show(new MinamiTextComponent("ESC For KEY_NONE.").addStyles(BOLD, YELLOW));
        show(new MinamiTextComponent("-----------------------------------").addStyles(GRAY));
    }

    public static MinamiTextComponent getCenterAlignText(MinamiTextComponent originalText) {
        int maxLength = 35;
        int originalLength = originalText.content.getString().length();
        if (originalLength >= maxLength) {
            return originalText;
        }
        return new MinamiTextComponent(" ".repeat((maxLength - originalLength) / 2))
                .append(originalText);
    }

}
