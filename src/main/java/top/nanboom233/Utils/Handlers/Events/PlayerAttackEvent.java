package top.nanboom233.Utils.Handlers.Events;

import net.minecraft.entity.Entity;

/**
 * Generated when the player attack
 *
 * @author Nanboom233
 * @since 2024/10/2
 */
public class PlayerAttackEvent extends EventTemplate {
    public Entity enemy;

    public PlayerAttackEvent(Entity enemy) {
        this.enemy = enemy;
    }
}
