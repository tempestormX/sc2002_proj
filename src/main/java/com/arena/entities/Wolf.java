package com.arena.entities;

import com.arena.core.Combatant;
import com.arena.ui.GameUI;

/**
 * Wolf – an aggressive pack animal with high speed.
 * Special Skill: <b>Pack Howl</b> – deals damage equal to 1.5× attack and
 * signals that a backup spawn may occur (placeholder for future implementation).
 */
public class Wolf extends Combatant {

    private static final String SPECIAL_NAME = "Pack Howl";
    private static final int BASE_HP      = 70;
    private static final int BASE_ATTACK  = 15;
    private static final int BASE_DEFENSE = 7;
    private static final int BASE_SPEED   = 18;

    public Wolf(String name) {
        super(name, BASE_HP, BASE_ATTACK, BASE_DEFENSE, BASE_SPEED);
    }

    @Override
    public String getSpecialSkillName() {
        return SPECIAL_NAME;
    }

    @Override
    public void executeSpecialSkill(Combatant target, GameUI ui) {
        int howlDamage = (int) (getAttack() * 1.5);
        target.takeDamage(howlDamage);
        ui.displayMessage(getName() + " lets out a " + SPECIAL_NAME + " and lunges at "
                + target.getName() + "! The howl may summon reinforcements soon…");
        // TODO (Backup Spawn): trigger backup spawn logic in BattleEngine
        //      when a Wolf uses Pack Howl and the enemy count drops below a threshold.
    }
}
