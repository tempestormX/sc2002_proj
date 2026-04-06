package turnbasedcombat.domain.action;
import turnbasedcombat.domain.character.Combatant;
import turnbasedcombat.domain.item.Item;
import java.util.List;

public class UseItem implements Action {
    private final Item item;

    public UseItem(Item item) { this.item = item; }
    @Override public String getName() { return "Use " + item.getName(); }
    @Override public String getDescription() { return item.getDescription(); }

    @Override
    public void execute(Combatant performer, Combatant target, List<Combatant> allCombatants) {
        item.use(performer, target, allCombatants);
        performer.removeItem(item);
    }

    @Override public boolean canExecute(Combatant performer) {
        return performer.isAlive() && performer.getInventory().contains(item);
    }
    public Item getItem() { return item; }
}