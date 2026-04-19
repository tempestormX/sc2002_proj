package turnbasedcombat.domain.action;

import turnbasedcombat.domain.character.Combatant;
import java.util.List;

public class SpecialSkill implements Action {

    @Override
    public String getName() {
        return "Special Skill";
    }

    @Override
    public String getDescription() {
        return "Use the character's unique special ability.";
    }

    @Override
    public void execute(Combatant performer, Combatant target, List<Combatant> allEnemies) {
        // 1. Execute the character's specific skill
        performer.executeSpecialSkill(target, allEnemies);
        
        // 2. Set the cooldown to 3 (you will need to add setSpecialCooldown() to Combatant)
        performer.setSpecialCooldown(3);
    }

    @Override
    public boolean canExecute(Combatant performer) {
        // Can only execute if alive AND cooldown is 0
        return performer.isAlive() && performer.getSpecialCooldown() == 0;
    }
}
