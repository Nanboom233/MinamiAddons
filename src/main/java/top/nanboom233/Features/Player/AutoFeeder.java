package top.nanboom233.Features.Player;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import top.nanboom233.Config.Keybind.MultiKeybind;
import top.nanboom233.Utils.ControlUtils;
import top.nanboom233.Utils.Module.ToggleModuleTemplate;

import java.util.HashMap;
import java.util.Set;

import static top.nanboom233.Config.Keybind.KeyCodes.KEY_NONE;
import static top.nanboom233.Config.Keybind.MultiKeybind.KeyActionType.PRESS;
import static top.nanboom233.Config.Keybind.MultiKeybind.scopeType.INGAME;
import static top.nanboom233.MinamiAddons.mc;

/**
 * The module to auto feed animals
 *
 * @author Nanboom233
 * @since 2025/6/28
 */
public class AutoFeeder extends ToggleModuleTemplate {
    public static final String moduleName = "AutoFeeder";
    public static final String description = "Feed animal automatically.";
    private static final MultiKeybind keybind = new MultiKeybind(Set.of(KEY_NONE), INGAME, PRESS, false);

    private static final HashMap<Class<? extends AnimalEntity>, Item> FEED_MAP = new HashMap<>();


    static {
        FEED_MAP.put(net.minecraft.entity.passive.CowEntity.class, net.minecraft.item.Items.WHEAT);
        FEED_MAP.put(net.minecraft.entity.passive.SheepEntity.class, net.minecraft.item.Items.WHEAT);
        FEED_MAP.put(net.minecraft.entity.passive.PigEntity.class, net.minecraft.item.Items.CARROT);
        FEED_MAP.put(net.minecraft.entity.passive.ChickenEntity.class, net.minecraft.item.Items.WHEAT_SEEDS);
        FEED_MAP.put(net.minecraft.entity.passive.WolfEntity.class, net.minecraft.item.Items.BONE);
        FEED_MAP.put(net.minecraft.entity.passive.RabbitEntity.class, net.minecraft.item.Items.CARROT);
        FEED_MAP.put(net.minecraft.entity.passive.LlamaEntity.class, net.minecraft.item.Items.HAY_BLOCK);

    }

    public AutoFeeder() {
        super(moduleName, description, keybind);
    }

    @Override
    protected void onEnable() {
        super.onEnable();
    }

    @Override
    protected void onDisable() {
        super.onDisable();
    }

    @Override
    protected void tick() {
        if (mc.player == null || mc.world == null || mc.interactionManager == null) {
            return;
        }

        Vec3d pos = mc.player.getPos();
        Box box = new Box(pos.add(-5, -5, -5), pos.add(5, 5, 5));

        for (Entity entity : mc.world.getOtherEntities(mc.player, box)) {
            if (entity instanceof AnimalEntity animal) {
                if (animal.isBaby() || animal.getLoveTicks() > 0) {
                    return;
                }

                Item requiredFood = FEED_MAP.get(animal.getClass());
                if (requiredFood == null) {
                    return;
                }

                int foodSlot = ControlUtils.findHotbarItem(requiredFood);
                if (foodSlot == -1) {
                    return;
                }

                if (ControlUtils.getCurrentSlot() != foodSlot) {
                    ControlUtils.setSlot(foodSlot);
                    return;
                }

                mc.interactionManager.interactEntity(mc.player, animal, Hand.MAIN_HAND);
//                return;
            }
        }
    }
}
