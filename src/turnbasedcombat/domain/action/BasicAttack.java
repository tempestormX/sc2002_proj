package turnbasedcombat.domain.action;

import turnbasedcombat.domain.character.Combatant;
import java.util.List;

public class BasicAttack implements Action {
    private final String name = "Basic Attack";
    private final String description = "A standard attack dealing damage based on attack power.";

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
        int damage = performer.getAttack();
        target.takeDamage(damage);
    }

    @Override
    public boolean canExecute(Combatant performer) {
        return performer.isAlive();
    }
}