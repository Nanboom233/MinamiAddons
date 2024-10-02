package top.nanboom233.Tests;

import net.minecraft.entity.damage.DamageSource;
import top.nanboom233.Utils.Texts.ChatUtils;
import top.nanboom233.Utils.Texts.MinamiTextComponent;

import static top.nanboom233.Utils.Texts.MinamiStyles.*;

/**
 * Show the damage the player has dealt and taken
 *
 * @author Nanboom233
 * @since 2024/10/2
 */
public class DamageOverlay {


//    @SubscribeEvent
//    public static void onAttack(PlayerAttackEvent event) {
//        if (!MinamiAddons.config.enableDamageOverlay) {
//            return;
//        }
//        if (mc.world == null || mc.player == null || event.enemy instanceof LivingEntity) {
//            return;
//        }
//
//    }

    public static void damageDealtCallback(float expectedDamage, float actualDamage, boolean critical) {
        MinamiTextComponent text = new MinamiTextComponent("◎ Damage Dealt: ");
        MinamiTextComponent damage = new MinamiTextComponent(String.valueOf(actualDamage)).addStyles(YELLOW);
        if (critical) {
            damage.addStyles(GOLD, BOLD).append(new MinamiTextComponent(" (Critical)").withStyles(GRAY));
        }
        if (Math.abs(expectedDamage - actualDamage) >= 0.1f) {
            damage.append(new MinamiTextComponent(" | Expected: ").withStyles(WHITE)
                    .append(new MinamiTextComponent(String.valueOf(expectedDamage)).withStyles(DARK_AQUA)));
        }
        ChatUtils.actionbarShow(text.append(damage), false);
    }

    public static void damageTakenCallback(DamageSource damageSource, float amount) {
        MinamiTextComponent text = new MinamiTextComponent("☠ Damage Taken: ");
        MinamiTextComponent damage = new MinamiTextComponent(String.valueOf(amount)).addStyles(DARK_RED);
        MinamiTextComponent source = new MinamiTextComponent(" (" + damageSource.getName() + ")").addStyles(GRAY);
        ChatUtils.actionbarShow(text.append(damage).append(source), false);
    }
}
