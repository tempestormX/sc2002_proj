package com.arena.entities;

import com.arena.core.Combatant;
import com.arena.ui.GameUI;

/**
 * Wizard – a glass-cannon mage with high attack but low HP/defense.
 * Special Skill: <b>Arcane Burst</b> – deals high magic damage that partially
 * ignores the target's defense (defense reduced by half for this hit).
 */
public class Wizard extends Combatant {

    private static final String SPECIAL_NAME = "Arcane Burst";
    private static final int BASE_HP      = 80;
    private static final int BASE_ATTACK  = 25;
    private static final int BASE_DEFENSE = 5;
    private static final int BASE_SPEED   = 14;

    public Wizard(String name) {
        super(name, BASE_HP, BASE_ATTACK, BASE_DEFENSE, BASE_SPEED);
    }

    @Override
    public String getSpecialSkillName() {
        return SPECIAL_NAME;
    }

    @Override
    public void executeSpecialSkill(Combatant target, GameUI ui) {
        // Arcane Burst partially bypasses defense: effective damage = ATK*2 - DEF/2.
        int magicDamage = Math.max(1, getAttack() * 2 - target.getDefense() / 2);
        // Inflate the raw value so that after standard defense absorption the net
        // damage equals the intended magicDamage value.
        int rawValue = magicDamage + target.getDefense();
        target.takeDamage(rawValue);
        ui.displayMessage(getName() + " unleashes " + SPECIAL_NAME + " on " + target.getName()
                + "! A powerful blast of arcane magic partially bypasses defense!");
    }
}
