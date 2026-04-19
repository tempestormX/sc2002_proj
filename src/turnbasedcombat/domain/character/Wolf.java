package turnbasedcombat.domain.character;

import turnbasedcombat.domain.strategy.EnemyStrategy;
import turnbasedcombat.domain.strategy.BasicAttackStrategy;
import turnbasedcombat.control.BattleEngine;
import java.util.List;

public class Wolf extends Combatant {
    private EnemyStrategy strategy;

    public Wolf(String name) {
        super(name, 40, 45, 5, 35);
        this.strategy = new BasicAttackStrategy();
    }

    @Override
    public void executeSpecialSkill(Combatant target, List<Combatant> allCombatants) {}

    @Override
    public void takeTurn(BattleEngine engine) {
        strategy.executeTurn(this, engine);
    }
}