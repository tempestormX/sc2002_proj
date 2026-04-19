package turnbasedcombat.domain.character;

import turnbasedcombat.domain.strategy.EnemyStrategy;
import turnbasedcombat.domain.strategy.BasicAttackStrategy;
import turnbasedcombat.control.BattleEngine;
import java.util.List;

public class Goblin extends Combatant {
    private EnemyStrategy strategy;

    public Goblin(String name) {
        super(name, 55, 35, 15, 25);
        this.strategy = new BasicAttackStrategy();
    }

    @Override
    public void executeSpecialSkill(Combatant target, List<Combatant> allCombatants) {}

    @Override
    public void takeTurn(BattleEngine engine) {
        strategy.executeTurn(this, engine);
    }
}