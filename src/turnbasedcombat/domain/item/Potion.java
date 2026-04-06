package turnbasedcombat.domain.item;

import turnbasedcombat.domain.character.Combatant;
import java.util.List;

public class Potion implements Item {
    private final String name = "Health Potion";
    private final String description = "Restores 100 HP.";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void use(Combatant user, Combatant target, List<Combatant> allCombatants) {
        target.heal(100);
    }
}