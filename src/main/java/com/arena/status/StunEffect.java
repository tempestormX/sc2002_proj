package com.arena.status;

import com.arena.core.Combatant;

/**
 * Stun effect – the afflicted combatant loses their turn while stunned.
 * The {@link com.arena.engine.BattleEngine} checks {@link Combatant#isStunned()}
 * before processing a combatant's action each round.
 */
public class StunEffect extends StatusEffect {

    public StunEffect(int duration) {
        super("Stun", duration);
    }

    @Override
    public void onApply(Combatant target) {
        target.setStunned(true);
    }

    @Override
    public void onTick(Combatant target) {
        // After the tick, if the effect is about to expire the stun flag is cleared.
        if (getRemainingDuration() <= 1) {
            target.setStunned(false);
        }
    }
}
