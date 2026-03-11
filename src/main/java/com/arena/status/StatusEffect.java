package com.arena.status;

import com.arena.core.Combatant;

/**
 * Represents a status effect that can be applied to a Combatant.
 * Status effects have a limited duration and may alter combatant behaviour
 * each round (e.g. Stun, Smoke Bomb).
 */
public abstract class StatusEffect {

    private final String name;
    private int remainingDuration;

    protected StatusEffect(String name, int duration) {
        this.name = name;
        this.remainingDuration = duration;
    }

    /** Returns the display name of this effect. */
    public String getName() {
        return name;
    }

    /** Returns the number of rounds remaining for this effect. */
    public int getRemainingDuration() {
        return remainingDuration;
    }

    /** Returns {@code true} when the effect has expired (duration reached zero). */
    public boolean isExpired() {
        return remainingDuration <= 0;
    }

    /**
     * Called immediately when this effect is applied to a combatant.
     *
     * @param target the combatant receiving this effect
     */
    public abstract void onApply(Combatant target);

    /**
     * Called at the start of each round while this effect is active.
     * Implementations should apply per-round consequences here.
     *
     * @param target the combatant carrying this effect
     */
    public abstract void onTick(Combatant target);

    /**
     * Called at the start of each round to decrement the remaining duration
     * and then invoke {@link #onTick(Combatant)}.
     *
     * @param target the combatant carrying this effect
     */
    public void tick(Combatant target) {
        onTick(target);
        remainingDuration--;
    }

    @Override
    public String toString() {
        return name + " (" + remainingDuration + " round(s) remaining)";
    }
}
