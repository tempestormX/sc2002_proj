package turnbasedcombat.domain.effect;

import turnbasedcombat.domain.character.Combatant;

public interface StatusEffect {
    void apply(Combatant target);
    void remove(Combatant target);
    void tick(Combatant target);
    boolean isExpired();
    String getName();
    int getRemainingDuration();
}
