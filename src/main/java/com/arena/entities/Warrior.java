package com.arena.entities;

import com.arena.core.Combatant;
import com.arena.ui.GameUI;

/**
 * Warrior – a melee fighter with high HP and defense.
 * Special Skill: <b>Shield Bash</b> – deals bonus damage and stuns the target
 * for one round.
 */
public class Warrior extends Combatant {

    private static final String SPECIAL_NAME = "Shield Bash";
    private static final int BASE_HP      = 120;
    private static final int BASE_ATTACK  = 18;
    private static final int BASE_DEFENSE = 12;
    private static final int BASE_SPEED   = 8;

    public Warrior(String name) {
        super(name, BASE_HP, BASE_ATTACK, BASE_DEFENSE, BASE_SPEED);
    }

    @Override
    public String getSpecialSkillName() {
        return SPECIAL_NAME;
    }

    @Override
    public void executeSpecialSkill(Combatant target, GameUI ui) {
        int bonusDamage = getAttack() + 10;
        target.takeDamage(bonusDamage);
        target.applyStatusEffect(new com.arena.status.StunEffect(1));
        ui.displayMessage(getName() + " uses " + SPECIAL_NAME + " on " + target.getName()
                + "! Deals bonus damage and stuns for 1 round!");
    }
}
