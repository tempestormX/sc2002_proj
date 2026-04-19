package turnbasedcombat.domain.effect;

import turnbasedcombat.domain.character.Combatant;

public class StunEffect implements StatusEffect {
    private int duration;
    private final String name = "Stun";

    public StunEffect(int duration) {
        this.duration = duration;
    }

    @Override
    public void apply(Combatant target) {
        // Stun effect is checked during turn execution
    }

    @Override
    public void remove(Combatant target) {
        // No cleanup needed
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
