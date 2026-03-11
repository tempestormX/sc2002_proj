package com.arena.action;

import com.arena.core.Combatant;
import com.arena.status.SmokeBombEffect;
import com.arena.ui.GameUI;

import java.util.Random;

/**
 * Basic Attack action – the actor deals damage equal to their attack stat
 * minus the target's defense.  If the actor is blinded (e.g. by a
 * {@link SmokeBombEffect}), there is a 50% chance the attack misses entirely.
 */
public class BasicAttack implements Action {

    private static final String NAME = "Basic Attack";
    private static final Random RANDOM = new Random();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void execute(Combatant actor, Combatant target, GameUI ui) {
        if (actor.isBlinded() && RANDOM.nextInt(100) < SmokeBombEffect.MISS_CHANCE_PERCENT) {
            ui.displayMessage(actor.getName() + " attacks " + target.getName()
                    + " but MISSES due to the smoke!");
            return;
        }

        target.takeDamage(actor.getAttack());
        ui.displayMessage(actor.getName() + " attacks " + target.getName()
                + " for " + actor.getAttack() + " raw damage.");
    }
}
