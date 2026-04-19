package turnbasedcombat.domain.item;

import turnbasedcombat.domain.character.Combatant;
import java.util.List;

public interface Item {
    String getName();
    String getDescription();
    void use(Combatant user, Combatant target, List<Combatant> allCombatants);
}