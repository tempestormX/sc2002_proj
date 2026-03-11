package com.arena.entities;

import com.arena.core.Combatant;
import com.arena.ui.GameUI;

/**
 * Goblin – a fast, opportunistic enemy with moderate stats.
 * Special Skill: <b>Smoke Bomb</b> – blinds the target for 2 rounds,
 * causing a percentage of their attacks to miss.
 */
public class Goblin extends Combatant {

    private static final String SPECIAL_NAME = "Smoke Bomb";
    private static final int BASE_HP      = 60;
    private static final int BASE_ATTACK  = 12;
    private static final int BASE_DEFENSE = 6;
    private static final int BASE_SPEED   = 16;

    public Goblin(String name) {
        super(name, BASE_HP, BASE_ATTACK, BASE_DEFENSE, BASE_SPEED);
    }

    @Override
    public String getSpecialSkillName() {
        return SPECIAL_NAME;
    }

    @Override
    public void executeSpecialSkill(Combatant target, GameUI ui) {
        target.applyStatusEffect(new com.arena.status.SmokeBombEffect(2));
        ui.displayMessage(getName() + " throws a " + SPECIAL_NAME + " at " + target.getName()
                + "! " + target.getName() + " is blinded for 2 rounds – accuracy reduced by 50%!");
    }
}
