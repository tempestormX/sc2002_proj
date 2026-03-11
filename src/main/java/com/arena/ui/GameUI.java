package com.arena.ui;

import com.arena.action.Action;
import com.arena.core.Combatant;

import java.util.List;
import java.util.Scanner;

/**
 * Boundary / UI class – all System.out and Scanner I/O is isolated here so
 * that the engine remains pure (no direct console access).  Swap this class
 * for a GUI or test double without touching any engine logic.
 */
public class GameUI {

    private final Scanner scanner;

    public GameUI() {
        this.scanner = new Scanner(System.in);
    }

    // -----------------------------------------------------------------------
    // Output methods
    // -----------------------------------------------------------------------

    /** Prints a blank separator line to the console. */
    public void displaySeparator() {
        System.out.println("─".repeat(60));
    }

    /** Prints a message followed by a newline. */
    public void displayMessage(String message) {
        System.out.println(message);
    }

    /** Displays the battle opening banner. */
    public void displayBattleStart() {
        displaySeparator();
        System.out.println("         ⚔   COMBAT ARENA – BATTLE START   ⚔");
        displaySeparator();
    }

    /**
     * Displays a round header.
     *
     * @param round current round number (1-indexed)
     */
    public void displayRoundStart(int round) {
        System.out.println();
        displaySeparator();
        System.out.println("  ROUND " + round);
        displaySeparator();
    }

    /** Displays the full status block for a single combatant. */
    public void displayCombatantStatus(Combatant combatant) {
        System.out.printf("  %-20s HP: %3d/%3d  ATK: %2d  DEF: %2d  SPD: %2d",
                combatant.getName(),
                combatant.getHp(), combatant.getMaxHp(),
                combatant.getAttack(), combatant.getDefense(), combatant.getSpeed());

        if (!combatant.getActiveEffects().isEmpty()) {
            System.out.print("  [" + combatant.getActiveEffects().stream()
                    .map(e -> e.getName())
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("") + "]");
        }
        System.out.println();
    }

    /** Displays a victory banner. */
    public void displayVictory() {
        displaySeparator();
        System.out.println("         ★   VICTORY! All enemies defeated!   ★");
        displaySeparator();
    }

    /** Displays a defeat banner. */
    public void displayDefeat() {
        displaySeparator();
        System.out.println("         ✗   DEFEAT! Your party has fallen…   ✗");
        displaySeparator();
    }

    /** Displays a notification that backup enemies have spawned. */
    public void displayBackupSpawn(String enemyName) {
        displaySeparator();
        System.out.println("  ⚠  BACKUP SPAWN: " + enemyName + " joins the battle!");
        displaySeparator();
    }

    // -----------------------------------------------------------------------
    // Input methods
    // -----------------------------------------------------------------------

    /**
     * Presents the available actions to the player and returns the chosen
     * {@link Action}.  Keeps prompting until a valid selection is made.
     *
     * @param actor   the player-controlled combatant whose turn it is
     * @param actions the list of available actions
     * @return the selected action
     */
    public Action promptAction(Combatant actor, List<Action> actions) {
        System.out.println();
        System.out.println(actor.getName() + "'s turn – choose an action:");
        for (int i = 0; i < actions.size(); i++) {
            Action a = actions.get(i);
            String label = a.getName();
            // Show the combatant-specific skill name for the SpecialSkill option.
            if (a instanceof com.arena.action.SpecialSkill) {
                label = actor.getSpecialSkillName() + " (Special)";
            }
            System.out.printf("  [%d] %s%n", i + 1, label);
        }
        System.out.print("Enter choice: ");

        while (true) {
            String input = scanner.nextLine().trim();
            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= actions.size()) {
                    return actions.get(choice - 1);
                }
            } catch (NumberFormatException ignored) {
                // fall through to error message
            }
            System.out.print("Invalid choice. Enter 1–" + actions.size() + ": ");
        }
    }

    /**
     * Presents the list of valid targets and returns the one chosen by the
     * player.  Keeps prompting until a valid selection is made.
     *
     * @param targets available targets (all living enemies)
     * @return the selected target
     */
    public Combatant promptTarget(List<Combatant> targets) {
        System.out.println("Choose a target:");
        for (int i = 0; i < targets.size(); i++) {
            System.out.printf("  [%d] %s (HP: %d/%d)%n",
                    i + 1,
                    targets.get(i).getName(),
                    targets.get(i).getHp(),
                    targets.get(i).getMaxHp());
        }
        System.out.print("Enter choice: ");

        while (true) {
            String input = scanner.nextLine().trim();
            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= targets.size()) {
                    return targets.get(choice - 1);
                }
            } catch (NumberFormatException ignored) {
                // fall through to error message
            }
            System.out.print("Invalid choice. Enter 1–" + targets.size() + ": ");
        }
    }

    /** Closes the underlying {@link Scanner}. */
    public void close() {
        scanner.close();
    }
}
