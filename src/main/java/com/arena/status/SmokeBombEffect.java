package com.arena.status;

import com.arena.core.Combatant;

/**
 * Smoke Bomb effect – the afflicted combatant has their accuracy reduced,
 * causing a percentage of their attacks to miss.
 * The {@link com.arena.engine.BattleEngine} checks {@link Combatant#isBlinded()}
 * when resolving attacks.
 */
public class SmokeBombEffect extends StatusEffect {

    /** Percentage chance (0–100) that an attack misses while blinded. */
    public static final int MISS_CHANCE_PERCENT = 50;

    public SmokeBombEffect(int duration) {
        super("Smoke Bomb", duration);
    }

    @Override
    public void onApply(Combatant target) {
        target.setBlinded(true);
    }

    @Override
    public void onTick(Combatant target) {
        // Clear the blinded flag when the effect is about to expire.
        if (getRemainingDuration() <= 1) {
            target.setBlinded(false);
        }
    }
}
