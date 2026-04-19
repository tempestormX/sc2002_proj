package turnbasedcombat.domain.strategy;

import turnbasedcombat.domain.character.Combatant;

import java.util.List;

import turnbasedcombat.control.BattleEngine;

public interface EnemyStrategy {
    void executeTurn(Combatant enemy, BattleEngine engine);
    
}
