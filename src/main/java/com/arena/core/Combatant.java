package com.arena.core;

import com.arena.status.StatusEffect;
import com.arena.ui.GameUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Abstract base class for every participant in combat.
 * Encapsulates shared attributes (HP, Attack, Defense, Speed) and the
 * status-effect pipeline.  Concrete subclasses provide entity-specific
 * special skills via {@link #getSpecialSkillName()} and
 * {@link #executeSpecialSkill(Combatant, GameUI)}.
 */
public abstract class Combatant {

    // -----------------------------------------------------------------------
    // Core attributes
    // -----------------------------------------------------------------------

    private final String name;
    private int hp;
    private final int maxHp;
    private final int attack;
    private int defense;
    private final int speed;

    // -----------------------------------------------------------------------
    // Status flags (set/cleared by StatusEffect implementations)
    // -----------------------------------------------------------------------

    private boolean stunned;
    private boolean blinded;
    private boolean defending;

    // -----------------------------------------------------------------------
    // Active status effects
    // -----------------------------------------------------------------------

    private final List<StatusEffect> activeEffects = new ArrayList<>();

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------

    protected Combatant(String name, int hp, int attack, int defense, int speed) {
        this.name    = name;
        this.maxHp   = hp;
        this.hp      = hp;
        this.attack  = attack;
        this.defense = defense;
        this.speed   = speed;
    }

    // -----------------------------------------------------------------------
    // Abstract interface – subclasses must implement
    // -----------------------------------------------------------------------

    /** Human-readable label shown in the action menu for this combatant's special skill. */
    public abstract String getSpecialSkillName();

    /**
     * Executes this combatant's unique special skill against the given target.
     *
     * @param target the combatant receiving the skill's effect
     * @param ui     the boundary class used to display messages
     */
    public abstract void executeSpecialSkill(Combatant target, GameUI ui);

    // -----------------------------------------------------------------------
    // Combat mechanics
    // -----------------------------------------------------------------------

    /**
     * Applies incoming raw damage after accounting for defense.
     * When the combatant is actively {@link #defending}, defense is doubled.
     *
     * @param rawDamage damage value before defense is applied
     */
    public void takeDamage(int rawDamage) {
        int effectiveDefense = defending ? defense * 2 : defense;
        int actualDamage = Math.max(1, rawDamage - effectiveDefense);
        hp = Math.max(0, hp - actualDamage);
    }

    /**
     * Restores HP by the given amount, capped at {@link #maxHp}.
     *
     * @param amount the amount of HP to restore
     */
    public void heal(int amount) {
        hp = Math.min(maxHp, hp + amount);
    }

    /** Returns {@code true} if this combatant still has HP remaining. */
    public boolean isAlive() {
        return hp > 0;
    }

    // -----------------------------------------------------------------------
    // Status-effect management
    // -----------------------------------------------------------------------

    /**
     * Adds a new {@link StatusEffect} to this combatant and immediately
     * invokes its {@link StatusEffect#onApply(Combatant)} callback.
     *
     * @param effect the effect to apply
     */
    public void applyStatusEffect(StatusEffect effect) {
        activeEffects.add(effect);
        effect.onApply(this);
    }

    /**
     * Ticks all active status effects (applies per-round consequences and
     * decrements their durations), then removes any that have expired.
     */
    public void tickStatusEffects() {
        activeEffects.forEach(e -> e.tick(this));
        activeEffects.removeIf(StatusEffect::isExpired);
    }

    /** Returns an unmodifiable view of the currently active status effects. */
    public List<StatusEffect> getActiveEffects() {
        return Collections.unmodifiableList(activeEffects);
    }

    // -----------------------------------------------------------------------
    // Getters & setters
    // -----------------------------------------------------------------------

    public String getName()    { return name; }
    public int getHp()         { return hp; }
    public int getMaxHp()      { return maxHp; }
    public int getAttack()     { return attack; }
    public int getDefense()    { return defense; }
    public int getSpeed()      { return speed; }

    public boolean isStunned()  { return stunned; }
    public boolean isBlinded()  { return blinded; }
    public boolean isDefending() { return defending; }

    public void setStunned(boolean stunned)   { this.stunned  = stunned; }
    public void setBlinded(boolean blinded)   { this.blinded  = blinded; }
    public void setDefending(boolean defending) { this.defending = defending; }

    @Override
    public String toString() {
        return String.format("%s [HP: %d/%d | ATK: %d | DEF: %d | SPD: %d]",
                name, hp, maxHp, attack, defense, speed);
    }
}
