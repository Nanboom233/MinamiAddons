package top.nanboom233.Features.OurCityUtils;

import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import top.nanboom233.Config.Keybind.MultiKeybind;
import top.nanboom233.Utils.Module.ToggleModuleTemplate;
import top.nanboom233.Utils.Texts.ChatUtils;

import java.util.Set;

import static top.nanboom233.Config.Keybind.KeyCodes.KEY_NONE;
import static top.nanboom233.Config.Keybind.MultiKeybind.KeyActionType.PRESS;
import static top.nanboom233.Config.Keybind.MultiKeybind.scopeType.INGAME;
import static top.nanboom233.MinamiAddons.mc;

public class FastCityChest extends ToggleModuleTemplate {
    public static final String moduleName = "FastCityChest";
    public static final String description = "Open the city's chest instantly.";
    private static long moduleStartTime = -1L;

    private static final MultiKeybind keyBinding = new MultiKeybind(
            Set.of(KEY_NONE), INGAME, PRESS, false);

    public FastCityChest() {
        super(moduleName, description, keyBinding);
    }

    @Override
    protected void onEnable() {
        moduleStartTime = System.currentTimeMillis();
        ChatUtils.send("/oc");
    }

    @Override
    protected void onDisable() {
    }

    @Override
    protected void tick() {
        if (mc.currentScreen instanceof GenericContainerScreen) {
            String title = ChatUtils.getCleanText(mc.currentScreen.getTitle());
            if ("main菜单".equals(title) || title.contains("城市菜单")) {
                if (mc.interactionManager != null) {
                    GenericContainerScreenHandler containerHandler = ((GenericContainerScreen) mc.currentScreen).getScreenHandler();
                    mc.interactionManager.clickSlot(containerHandler.syncId, 8, 0, SlotActionType.PICKUP, mc.player);
                }
            }
            if (title.contains("小镇箱子") || System.currentTimeMillis() - moduleStartTime > 3000L) {
                toggle();
            }
        }
    }
}