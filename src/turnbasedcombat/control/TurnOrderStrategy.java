package turnbasedcombat.control;

import turnbasedcombat.domain.character.Combatant;
import java.util.List;

public interface TurnOrderStrategy {
    List<Combatant> determineOrder(List<Combatant> combatants);
}
