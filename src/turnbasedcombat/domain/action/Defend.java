package turnbasedcombat.domain.action;

import turnbasedcombat.domain.character.Combatant;
import turnbasedcombat.domain.effect.DefendEffect;
import java.util.List;

public class Defend implements Action {
    private final String name = "Defend";
    private final String description = "Increases Defense by 10 for current and next round.";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void execute(Combatant performer, Combatant target, List<Combatant> allCombatants) {
        performer.addStatusEffect(new DefendEffect(2));
    }

    @Override
    public boolean canExecute(Combatant performer) {
        return performer.isAlive();
    }
}
