package top.nanboom233.Utils.Module;

import org.jetbrains.annotations.NotNull;
import top.nanboom233.Features.OurCityUtils.FastCityChest;
import top.nanboom233.Features.Player.AttackSwitcher;
import top.nanboom233.Features.Player.AutoFeeder;
import top.nanboom233.Features.Player.SmartClicker;
import top.nanboom233.Features.World.GhostBlock;
import top.nanboom233.Tests.ExampleInstantModule;
import top.nanboom233.Tests.ExampleToggleModule;
import top.nanboom233.Utils.Handlers.Events.TickEndEvent;
import top.nanboom233.Utils.Handlers.SubscribeEvent;

import java.util.ArrayList;

/**
 * @author Nanboom233
 */
public class ModuleManager {
    private static final ArrayList<ModuleTemplate> moduleList = new ArrayList<>();
    private static final ModuleManager INSTANCE = new ModuleManager();

    public static ModuleManager getInstance() {
        return INSTANCE;
    }

    @SubscribeEvent
    public static void moduleTriggerHandle(TickEndEvent event) {
        long curTime = System.currentTimeMillis();
        for (ModuleTemplate module : moduleList) {
            if (module.keybind.triggered()) {
                module.trigger();
            }
            if (module instanceof ToggleModuleTemplate && ((ToggleModuleTemplate) module).state) {
                ((ToggleModuleTemplate) module).tick();
            }
        }
    }

    public ModuleManager() {
        register(new ExampleToggleModule());
        register(new ExampleInstantModule());
        register(new GhostBlock());
        register(new FastCityChest());
        register(new AttackSwitcher());
        register(new SmartClicker());
        register(new AutoFeeder());
    }

    public void register(@NotNull ModuleTemplate module) {
        moduleList.add(module);
    }

    public ArrayList<ModuleTemplate> getModuleList() {
        return moduleList;
    }
}