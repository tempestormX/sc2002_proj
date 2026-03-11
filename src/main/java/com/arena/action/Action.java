package com.arena.action;

import com.arena.core.Combatant;
import com.arena.ui.GameUI;

/**
 * Command Pattern – represents any action a {@link Combatant} can take
 * during their turn.  Each concrete implementation encapsulates the
 * complete logic for one type of action (attack, defend, special skill, etc.).
 */
public interface Action {

    /**
     * Executes this action with the given actor targeting the given combatant.
     *
     * @param actor  the combatant performing the action
     * @param target the combatant receiving the action's effect
     * @param ui     boundary class used to display result messages
     */
    void execute(Combatant actor, Combatant target, GameUI ui);

    /**
     * Returns the display name of this action shown in the action menu.
     *
     * @return human-readable action name
     */
    String getName();
}
