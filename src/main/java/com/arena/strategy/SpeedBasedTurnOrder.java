package com.arena.strategy;

import com.arena.core.Combatant;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Speed-based turn order – combatants with higher {@link Combatant#getSpeed()}
 * act first.  Ties are broken by name to ensure a deterministic, reproducible
 * order.
 */
public class SpeedBasedTurnOrder implements TurnOrderStrategy {

    @Override
    public List<Combatant> determineTurnOrder(List<Combatant> combatants) {
        List<Combatant> ordered = new ArrayList<>(combatants);
        ordered.sort(
            Comparator.comparingInt(Combatant::getSpeed)
                      .reversed()
                      .thenComparing(Combatant::getName)
        );
        return ordered;
    }
}
