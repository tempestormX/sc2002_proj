package turnbasedcombat.control;

import turnbasedcombat.domain.character.Combatant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SpeedTurnStrategy implements TurnOrderStrategy {

    @Override
    public List<Combatant> determineOrder(List<Combatant> combatants) {
        List<Combatant> aliveCombatants = new ArrayList<>();

        for (Combatant c : combatants) {
            if (c.isAlive()) {
                aliveCombatants.add(c);
            }
        }

        // Sort descending by speed
        aliveCombatants.sort(Comparator.comparingInt(Combatant::getSpeed).reversed());

        return aliveCombatants;
    }
}