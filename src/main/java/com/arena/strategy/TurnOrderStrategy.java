package com.arena.strategy;

import com.arena.core.Combatant;

import java.util.List;

/**
 * Strategy Pattern – encapsulates the algorithm used to determine the order
 * in which combatants act during a round.  Swapping implementations allows
 * the engine to support different initiative systems without modification.
 */
public interface TurnOrderStrategy {

    /**
     * Returns a new list containing the given combatants in the order they
     * should take their turns this round.
     *
     * @param combatants all living combatants participating in the round
     * @return ordered list (first element acts first)
     */
    List<Combatant> determineTurnOrder(List<Combatant> combatants);
}
