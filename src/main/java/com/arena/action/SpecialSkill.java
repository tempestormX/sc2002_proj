package com.arena.action;

import com.arena.core.Combatant;
import com.arena.ui.GameUI;

/**
 * Special Skill action – delegates to the actor's own
 * {@link Combatant#executeSpecialSkill(Combatant, GameUI)} method, keeping
 * entity-specific logic inside each subclass while conforming to the
 * {@link Action} command interface.
 */
public class SpecialSkill implements Action {

    @Override
    public String getName() {
        // The actual display name comes from the actor at execution time.
        return "Special Skill";
    }

    @Override
    public void execute(Combatant actor, Combatant target, GameUI ui) {
        actor.executeSpecialSkill(target, ui);
    }
}
