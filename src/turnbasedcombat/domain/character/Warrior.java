package turnbasedcombat.domain.character;

import turnbasedcombat.domain.effect.StunEffect;
import turnbasedcombat.domain.action.Action;
import turnbasedcombat.control.BattleEngine;
import java.util.List;

public class Warrior extends Combatant {
    public Warrior(String name) {
        super(name, 260, 40, 20, 30); 
    }

    @Override
    public void executeSpecialSkill(Combatant target, List<Combatant> allCombatants) {
        target.takeDamage(this.attack);

       //Apply stun effect for 3 turns, avoiding attack on round 6
        target.addStatusEffect(new StunEffect(3));
    }

    @Override
    public void takeTurn(BattleEngine engine) {
        Action action = engine.getCli().getPlayerAction(this);
        Combatant target = engine.getCli().getPlayerTarget(this, action, engine.getEnemyTeam(), engine.getPlayerTeam());
        
        if (target != null && action.canExecute(this)) {
            engine.getCli().displayActionExecution(this, action, target);
            action.execute(this, target, engine.getAllCombatants());
            engine.getCli().displayActionResult(this, action, target);
        }
    }
}