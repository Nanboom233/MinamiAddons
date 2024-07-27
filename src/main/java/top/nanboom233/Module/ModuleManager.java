package top.nanboom233.Module;

import top.nanboom233.Features.OurCityUtils.FastCityChest;
import top.nanboom233.Features.World.GhostBlock;
import top.nanboom233.Handlers.TickHandler;
import top.nanboom233.MinamiAddons;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Nanboom233
 */
public class ModuleManager {
    private static final ArrayList<ModuleTemplate> moduleList = new ArrayList<>();
    private static final ModuleManager INSTANCE = new ModuleManager();

    public static ModuleManager getInstance() {
        return INSTANCE;
    }

    private static long lastAlert = -1;

    public final TickHandler.ITickTask moduleTriggerHandle = mc -> {
        long curTime = System.currentTimeMillis();
        for (ModuleTemplate module : moduleList) {
            if (module == null) {
                if (curTime - lastAlert > 2000L) {
                    MinamiAddons.getLogger().fatal("Null module was detected and will be automatically removed.");
                    lastAlert = curTime;
                }
                break;
            }
            if (module.keybind.isTriggered()) {
                module.trigger();
            }
            if (module instanceof ToggleModuleTemplate && ((ToggleModuleTemplate) module).state) {
                ((ToggleModuleTemplate) module).tick();
            }
        }
        if (curTime - lastAlert < 5000L) {
            moduleList.removeIf(Objects::isNull);
            MinamiAddons.getLogger().fatal("Current length of the moduleList:" + moduleList.size());
        }
    };

    public ModuleManager() {
        register(new ExampleToggleModule());
        register(new ExampleInstantModule());
        register(new GhostBlock());
        register(new FastCityChest());

        TickHandler.getInstance().register(moduleTriggerHandle);
    }

    public void register(ModuleTemplate module) {
        if (module != null && !moduleExists(module)) {
            moduleList.add(module);
        }
    }

    public boolean moduleExists(ModuleTemplate module) {
        for (ModuleTemplate m : moduleList) {
            if (m.moduleName.equals(module.moduleName)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<ModuleTemplate> getModuleList() {
        return moduleList;
    }
}