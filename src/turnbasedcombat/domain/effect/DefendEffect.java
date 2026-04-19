package turnbasedcombat.domain.effect;

import turnbasedcombat.domain.character.Combatant;

public class DefendEffect implements StatusEffect {
    private int duration;
    private final String name = "Defending";
    private final int defenseBoost = 10; 

    public DefendEffect(int duration) {
        this.duration = duration;
    }

    @Override
    public void apply(Combatant target) {
        target.modifyDefense(defenseBoost);
    }

    @Override
    public void remove(Combatant target) {
        target.modifyDefense(-defenseBoost);
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
