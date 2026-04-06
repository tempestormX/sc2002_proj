package turnbasedcombat.boundary;

import turnbasedcombat.domain.character.Combatant;
import turnbasedcombat.domain.action.Action;
import turnbasedcombat.domain.action.BasicAttack;
import turnbasedcombat.domain.action.Defend;
import turnbasedcombat.domain.action.SpecialSkill;
import turnbasedcombat.domain.action.UseItem;
import turnbasedcombat.domain.item.Item;
import turnbasedcombat.domain.effect.StatusEffect;
import java.util.List;
import java.util.Scanner;

public class GameCLI {
    private final Scanner scanner;

    public GameCLI() {
        this.scanner = new Scanner(System.in);
    }

    public void displayBattleStart(List<Combatant> playerTeam, List<Combatant> enemyTeam) {
        System.out.println("\n========================================");
        System.out.println("           BATTLE START!");
        System.out.println("========================================");
        System.out.println("\nYour team:");
        for (Combatant c : playerTeam) {
            displayCombatantStatus(c);
        }
        System.out.println("\nEnemy team:");
        for (Combatant c : enemyTeam) {
            displayCombatantStatus(c);
        }
        System.out.println("========================================\n");
    }

    public void displayTurnStart(int turnNumber) {
        System.out.println("\n--- Turn " + turnNumber + " ---");
    }

    public void displayTurnEnd(List<Combatant> playerTeam, List<Combatant> enemyTeam) {
        System.out.println("\n-- End of Turn Status --");
        System.out.println("Your team:");
        for (Combatant c : playerTeam) {
            if (c.isAlive()) {
                displayCombatantStatus(c);
            }
        }
        System.out.println("Enemy team:");
        for (Combatant c : enemyTeam) {
            if (c.isAlive()) {
                displayCombatantStatus(c);
            }
        }
    }

    public void displayCombatantTurn(Combatant combatant) {
        System.out.println("\n>> " + combatant.getName() + "'s turn!");
        displayCombatantStatus(combatant);
    }

    public void displayCombatantStatus(Combatant combatant) {
        System.out.printf("  %s: HP %d/%d | ATK %d | DEF %d | SPD %d | Cooldown %d%n",
                combatant.getName(),
                combatant.getHp(), combatant.getMaxHp(),
                combatant.getAttack(), combatant.getDefense(), 
                combatant.getSpeed(), combatant.getSpecialCooldown());

        List<StatusEffect> effects = combatant.getStatusEffects();
        if (!effects.isEmpty()) {
            System.out.print("    Status: ");
            for (int i = 0; i < effects.size(); i++) {
                StatusEffect effect = effects.get(i);
                System.out.print(effect.getName() + "(" + effect.getRemainingDuration() + ")");
                if (i < effects.size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println();
        }
    }

    public void displayStunned(Combatant combatant) {
        System.out.println(combatant.getName() + " is stunned and cannot act!");
    }

    public Action getPlayerAction(Combatant combatant) {
        System.out.println("\nChoose an action:");
        System.out.println("  1. Basic Attack");
        System.out.println("  2. Defend");
        System.out.printf("  3. Special Skill (Cooldown: %d)%n", combatant.getSpecialCooldown());

        List<Item> inventory = combatant.getInventory();
        if (!inventory.isEmpty()) {
            System.out.println("  4. Use Item");
        }

        int choice = getIntInput(1, inventory.isEmpty() ? 3 : 4);

        switch (choice) {
            case 1:
                return new BasicAttack();
            case 2:
                return new Defend();
            case 3:
                if (combatant.getSpecialCooldown() == 0) {
                    return new SpecialSkill();
                } else {
                    System.out.println("Skill is on cooldown! Using Basic Attack instead.");
                    return new BasicAttack();
                }
            case 4:
                return selectItem(combatant);
            default:
                return new BasicAttack();
        }
    }

    private Action selectItem(Combatant combatant) {
        List<Item> inventory = combatant.getInventory();
        System.out.println("\nSelect an item:");
        for (int i = 0; i < inventory.size(); i++) {
            Item item = inventory.get(i);
            System.out.printf("  %d. %s - %s%n", i + 1, item.getName(), item.getDescription());
        }
        System.out.println("  " + (inventory.size() + 1) + ". Cancel");

        int choice = getIntInput(1, inventory.size() + 1);

        if (choice == inventory.size() + 1) {
            return getPlayerAction(combatant);
        }

        return new UseItem(inventory.get(choice - 1));
    }

    public Combatant getPlayerTarget(Combatant performer, Action action,
                                      List<Combatant> enemies, List<Combatant> allies) {
        if (action instanceof Defend) {
            return performer;
        }

        boolean targetSelf = false;
        if (action instanceof UseItem) {
            Item item = ((UseItem)action).getItem();
            // Power Stone targets an enemy so the skill can hit them!
            if (item.getName().equals("Power Stone")) {
                targetSelf = false; 
            } else {
                targetSelf = true; // Potions and Smoke Bombs target self
            }
        }

        List<Combatant> possibleTargets = targetSelf ? allies : enemies;

        List<Combatant> aliveTargets = possibleTargets.stream()
                .filter(Combatant::isAlive)
                .toList();

        if (aliveTargets.isEmpty()) return null;
        if (aliveTargets.size() == 1) return aliveTargets.get(0);

        System.out.println("\nSelect a target:");
        for (int i = 0; i < aliveTargets.size(); i++) {
            Combatant target = aliveTargets.get(i);
            System.out.printf("  %d. %s (HP: %d/%d)%n",
                    i + 1, target.getName(), target.getHp(), target.getMaxHp());
        }

        int choice = getIntInput(1, aliveTargets.size());
        return aliveTargets.get(choice - 1);
    }

    public void displayActionExecution(Combatant performer, Action action, Combatant target) {
        System.out.printf("%s uses %s on %s!%n",
                performer.getName(), action.getName(), target.getName());
    }

    public void displayActionResult(Combatant performer, Action action, Combatant target) {
        if (!target.isAlive()) {
            System.out.println(target.getName() + " has been defeated!");
        } else {
            System.out.printf("%s now has %d/%d HP%n",
                    target.getName(), target.getHp(), target.getMaxHp());
        }
    }

    public void displayBattleEnd(boolean playerWon) {
        System.out.println("\n========================================");
        if (playerWon) {
            System.out.println("           VICTORY!");
        } else {
            System.out.println("           DEFEAT...");
        }
        System.out.println("========================================\n");
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public int getIntInput(int min, int max) {
        while (true) {
            System.out.print("Enter choice (" + min + "-" + max + "): ");
            try {
                String input = scanner.nextLine().trim();
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Please enter a number between " + min + " and " + max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    public String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public void startGame() {
        System.out.println("=========================================");
        System.out.println("Welcome to the Turn-Based Combat Game!");
        System.out.println("Course: SC2002 - OOP");
        System.out.println("Group: xxx");
        System.out.println("Group members: xxx,yyy,zzz");
        System.out.println("=========================================\n");
    }
}