package turnbasedcombat.domain.effect;

import turnbasedcombat.domain.character.Combatant;

public class ArcaneBlastBoost implements StatusEffect {
    private int duration;
    private final int attackBoost;
    private final String name = "Arcane Boost";

    public ArcaneBlastBoost(int duration, int attackBoost) {
        this.duration = duration;
        this.attackBoost = attackBoost;
    }

    @Override
    public void apply(Combatant target) {
        target.modifyAttack(attackBoost);
    }

    @Override
    public void remove(Combatant target) {
        target.modifyAttack(-attackBoost);
    }

    @Override
    public void tick(Combatant target) {
        duration--;
    }

    @Override
    public boolean isExpired() {
        return duration <= 0;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getRemainingDuration() {
        return duration;
    }
}
