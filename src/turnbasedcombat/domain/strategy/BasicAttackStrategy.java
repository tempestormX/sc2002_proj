package turnbasedcombat.domain.strategy;

import turnbasedcombat.domain.character.Combatant;
import turnbasedcombat.control.BattleEngine;
import turnbasedcombat.domain.action.BasicAttack;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BasicAttackStrategy implements EnemyStrategy {
    private final BasicAttack basicAttack = new BasicAttack();
    private final Random random = new Random();

    @Override
    public void executeTurn(Combatant enemy, BattleEngine engine) {
        List<Combatant> targets = engine.getPlayerTeam();
        List<Combatant> aliveTargets = new ArrayList<>();
        
        for (Combatant c : targets) {
            if (c.isAlive()) {
                aliveTargets.add(c);
            }
        }

        if (aliveTargets.isEmpty()) return;

        // Pick a random alive player
        Combatant target = aliveTargets.get(random.nextInt(aliveTargets.size()));
        
        // Execute the attack and tell the CLI to print it
        engine.getCli().displayActionExecution(enemy, basicAttack, target);
        basicAttack.execute(enemy, target, engine.getAllCombatants());
        engine.getCli().displayActionResult(enemy, basicAttack, target);
    }
}