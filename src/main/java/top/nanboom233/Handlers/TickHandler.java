package top.nanboom233.Handlers;

import net.minecraft.client.MinecraftClient;
import top.nanboom233.MinamiAddons;

import java.util.ArrayList;
import java.util.Objects;

public class TickHandler {
    private static final TickHandler INSTANCE = new TickHandler();
    private static final ArrayList<ITickTask> tasks = new ArrayList<>();
    private static final ArrayList<ITickTask> removalQueue = new ArrayList<>();
    private long lastAlert = -1;

    public static TickHandler getInstance() {
        return INSTANCE;
    }

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
            tasks.removeAll(removalQueue);
            removalQueue.clear();
        }
    }

    public void register(ITickTask task) {
        tasks.add(task);
    }

    public void unregister(ITickTask task) {
        removalQueue.add(task);
    }

    @FunctionalInterface
    public interface ITickTask {
        void onClientEndTick(MinecraftClient mc);
    }
}
