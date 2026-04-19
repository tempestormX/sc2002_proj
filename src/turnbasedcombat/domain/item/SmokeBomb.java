package turnbasedcombat.domain.item;

import turnbasedcombat.domain.character.Combatant;
import java.util.List;

public class SmokeBomb implements Item {
    private final String name = "Smoke Bomb";
    private final String description = "Enemy attacks deal exactly 0 damage for current turn and next turn.";
    private final int duration = 2;

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
        target.addStatusEffect(new turnbasedcombat.domain.effect.SmokeBombEffect(duration));
    }
}