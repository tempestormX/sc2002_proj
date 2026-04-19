package turnbasedcombat.domain.action;
import turnbasedcombat.domain.character.Combatant;
import java.util.List;

public interface Action {
    String getName();
    String getDescription();
    void execute(Combatant performer, Combatant target, List<Combatant> allCombatants);
    boolean canExecute(Combatant performer);
}