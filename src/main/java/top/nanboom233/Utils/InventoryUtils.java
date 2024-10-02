package top.nanboom233.Utils;

import net.minecraft.item.Item;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.intprovider.UniformIntProvider;

/**
 * @author Nanboom233
 * @since 2024/10/2
 */
public class InventoryUtils {

    public static int findItem(ScreenHandler container, Item item, UniformIntProvider... ranges) {
        final int slotMax = container.slots.size() - 1;

        for (UniformIntProvider range : ranges) {

            for (int slotNumber = range.getMin(); slotNumber <= range.getMax(); slotNumber++) {
                Slot slot = container.getSlot(slotNumber);

                if (slot.getStack().getItem().equals(item)) {
                    return slot.id;
                }
            }
        }

        return -1;
    }
}
