package turnbasedcombat.domain.effect;

import turnbasedcombat.domain.character.Combatant;

public class SmokeBombEffect implements StatusEffect {
    private int duration;
    private final String name = "Smoke Screen";
    
    // A massive defense boost ensures incoming damage math always results in 0
    private final int damageNegationBoost = 9999; 

    public SmokeBombEffect(int duration) {
        this.duration = duration;
    }

    @Override
    public void apply(Combatant target) {
        target.modifyDefense(damageNegationBoost);
    }

    @Override
    public void remove(Combatant target) {
        target.modifyDefense(-damageNegationBoost);
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