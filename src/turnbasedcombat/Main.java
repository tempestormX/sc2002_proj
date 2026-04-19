package turnbasedcombat;

import turnbasedcombat.boundary.GameCLI;
import turnbasedcombat.control.BattleEngine;
import turnbasedcombat.control.SpeedTurnStrategy;
import turnbasedcombat.control.TurnOrderStrategy;
import turnbasedcombat.domain.Level;
import turnbasedcombat.domain.character.Combatant;
import turnbasedcombat.domain.character.Warrior;
import turnbasedcombat.domain.character.Wizard;
import turnbasedcombat.domain.item.Item;
import turnbasedcombat.domain.item.Potion;
import turnbasedcombat.domain.item.PowerStone;
import turnbasedcombat.domain.item.SmokeBomb;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        GameCLI cli = new GameCLI();
        
        
        cli.displayMessage("========================================");
        cli.displayMessage("    WELCOME TO TURN-BASED COMBAT!       ");
        cli.displayMessage("========================================");
        cli.displayMessage("\nSelect your Class:");
        cli.displayMessage("1. Warrior (HP: 260, ATK: 40, DEF: 20, SPD: 30)");
        cli.displayMessage("2. Wizard  (HP: 200, ATK: 50, DEF: 10, SPD: 20)");
        
        int classChoice = cli.getIntInput(1, 2);
        Combatant player = (classChoice == 1) ? new Warrior("Hero (Warrior)") : new Wizard("Hero (Wizard)");

        cli.displayMessage("\nSelect two items for your inventory:");
        cli.displayMessage("1. Potion (Heals 100 HP)");
        cli.displayMessage("2. Power Stone (Free Special Skill)");
        cli.displayMessage("3. Smoke Bomb (0 Damage for 2 turns)");
        
        for (int i = 1; i <= 2; i++) {
            cli.displayMessage("Choose item " + i + ":");
            int itemChoice = cli.getIntInput(1, 3);
            if (itemChoice == 1) player.addItem(new Potion());
            else if (itemChoice == 2) player.addItem(new PowerStone());
            else if (itemChoice == 3) player.addItem(new SmokeBomb());
        }

        List<Combatant> playerTeam = new ArrayList<>();
        playerTeam.add(player);

        cli.displayMessage("\nSelect Difficulty Level:");
        cli.displayMessage("1. Easy   (3 Goblins)");
        cli.displayMessage("2. Medium (1 Goblin, 1 Wolf + Backup Wolves)");
        cli.displayMessage("3. Hard   (2 Goblins + Backup Goblin & Wolves)");
        
        int levelChoice = cli.getIntInput(1, 3);
        Level level = new Level(levelChoice);

        TurnOrderStrategy strategy = new SpeedTurnStrategy();
        BattleEngine engine = new BattleEngine(playerTeam, level, strategy, cli);
        
        engine.runBattle(); 
    }
}