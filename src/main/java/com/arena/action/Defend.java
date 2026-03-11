package com.arena.action;

import com.arena.core.Combatant;
import com.arena.ui.GameUI;

/**
 * Defend action – the actor braces for incoming attacks this round.
 * While defending, the {@link Combatant#takeDamage(int)} method doubles the
 * effective defense value, halving incoming damage.  The defending flag is
 * automatically reset at the start of the next round by the
 * {@link com.arena.engine.BattleEngine}.
 */
public class Defend implements Action {

    private static final String NAME = "Defend";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void execute(Combatant actor, Combatant target, GameUI ui) {
        actor.setDefending(true);
        ui.displayMessage(actor.getName() + " takes a defensive stance, doubling defense this round!");
    }
}
