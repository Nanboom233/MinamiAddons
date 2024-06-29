package top.nanboom233.Handlers;

import net.minecraft.client.MinecraftClient;
import top.nanboom233.MinamiAddons;

import java.util.ArrayList;
import java.util.Objects;

public class TickHandler {
    private static final TickHandler INSTANCE = new TickHandler();
    private static final ArrayList<ITickTask> tasks = new ArrayList<>();

    public static TickHandler getInstance() {
        return INSTANCE;
    }

    private long lastAlert = -1;

    public void onClientEndTick(MinecraftClient mc) {
        if (!tasks.isEmpty()) {
            long curTime = System.currentTimeMillis();

            for (ITickTask task : tasks) {
                if (task == null) {
                    if (curTime - lastAlert > 2000L) {
                        MinamiAddons.getLogger().fatal("Null ClientEndTask was detected and will be automatically removed.");
                        lastAlert = curTime;
                    }
                    break;
                }
                task.onClientEndTick(mc);
            }
            if (curTime - lastAlert < 5000L) {
                tasks.removeIf(Objects::isNull);
                MinamiAddons.getLogger().fatal("Current length of the tasks:" + tasks.size());
            }
        }
    }

    public void register(ITickTask task) {
        tasks.add(task);
    }

    @FunctionalInterface
    public interface ITickTask {
        void onClientEndTick(MinecraftClient mc);
    }
}
