package com.arena.engine;

import com.arena.action.Action;
import com.arena.action.BasicAttack;
import com.arena.action.Defend;
import com.arena.action.SpecialSkill;
import com.arena.core.Combatant;
import com.arena.strategy.TurnOrderStrategy;
import com.arena.ui.GameUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * BattleEngine – the central controller that drives the combat loop.
 * Responsibilities:
 * <ul>
 *   <li>Manages the game loop (round iteration until a win/loss condition).</li>
 *   <li>Determines turn order via the injected {@link TurnOrderStrategy}.</li>
 *   <li>Routes player turns to {@link GameUI} for input and AI turns to a
 *       simple random strategy.</li>
 *   <li>Applies and ticks {@link com.arena.status.StatusEffect}s each round.</li>
 *   <li>Detects win (all enemies dead) and loss (all players dead) conditions.</li>
 *   <li>Contains a placeholder hook for the <em>Backup Spawn</em> feature.</li>
 * </ul>
 *
 * <p>The engine itself never writes to {@code System.out} directly – all output
 * is delegated to {@link GameUI}, maintaining a clean separation of concerns.</p>
 */
public class BattleEngine {

    private static final List<Action> AVAILABLE_ACTIONS = List.of(
            new BasicAttack(),
            new Defend(),
            new SpecialSkill()
    );

    private final List<Combatant> players;
    private final List<Combatant> enemies;
    private final TurnOrderStrategy turnOrderStrategy;
    private final GameUI ui;
    private final Random random = new Random();

    private int currentRound = 0;

    /**
     * Constructs a BattleEngine.
     *
     * @param players           player-controlled combatants
     * @param enemies           AI-controlled combatants
     * @param turnOrderStrategy strategy used to sort combatants each round
     * @param ui                boundary class for all console I/O
     */
    public BattleEngine(List<Combatant> players,
                        List<Combatant> enemies,
                        TurnOrderStrategy turnOrderStrategy,
                        GameUI ui) {
        this.players           = new ArrayList<>(players);
        this.enemies           = new ArrayList<>(enemies);
        this.turnOrderStrategy = turnOrderStrategy;
        this.ui                = ui;
    }

    // -----------------------------------------------------------------------
    // Public API
    // -----------------------------------------------------------------------

    /**
     * Starts the battle and runs until either a win or loss condition is met.
     */
    public void startBattle() {
        ui.displayBattleStart();

        while (!isBattleOver()) {
            currentRound++;
            runRound();
            triggerBackupSpawnIfNeeded(); // placeholder hook
        }

        if (isVictory()) {
            ui.displayVictory();
        } else {
            ui.displayDefeat();
        }
    }

    // -----------------------------------------------------------------------
    // Round management
    // -----------------------------------------------------------------------

    /**
     * Runs a single round:
     * <ol>
     *   <li>Displays round header and all combatant statuses.</li>
     *   <li>Determines turn order.</li>
     *   <li>Ticks status effects for each active combatant.</li>
     *   <li>Processes each combatant's turn (player input or AI).</li>
     *   <li>Resets the defending flag at the end of the round.</li>
     * </ol>
     */
    void runRound() {
        ui.displayRoundStart(currentRound);
        displayAllStatuses();

        List<Combatant> allLiving = getAllLiving();
        List<Combatant> turnOrder = turnOrderStrategy.determineTurnOrder(allLiving);

        // Tick status effects before actions resolve.
        turnOrder.forEach(Combatant::tickStatusEffects);

        for (Combatant actor : turnOrder) {
            if (!actor.isAlive()) continue;
            if (isBattleOver()) break;

            if (actor.isStunned()) {
                ui.displayMessage(actor.getName() + " is stunned and cannot act this round!");
                continue;
            }

            if (isPlayer(actor)) {
                handlePlayerTurn(actor);
            } else {
                handleAiTurn(actor);
            }

            // Reset defending flag after the combatant has taken their turn.
            actor.setDefending(false);
        }

        // Remove dead combatants from both sides.
        players.removeIf(c -> !c.isAlive());
        enemies.removeIf(c -> !c.isAlive());
    }

    // -----------------------------------------------------------------------
    // Win / loss checks
    // -----------------------------------------------------------------------

    /**
     * Returns {@code true} when all combatants on at least one side have
     * been defeated.
     *
     * @return {@code true} if the battle should end
     */
    public boolean isBattleOver() {
        return players.stream().noneMatch(Combatant::isAlive)
                || enemies.stream().noneMatch(Combatant::isAlive);
    }

    /**
     * Returns {@code true} when all enemies have been defeated.
     *
     * @return {@code true} for a player victory
     */
    public boolean isVictory() {
        return enemies.stream().noneMatch(Combatant::isAlive);
    }

    // -----------------------------------------------------------------------
    // Backup spawn – placeholder
    // -----------------------------------------------------------------------

    /**
     * Placeholder hook called at the end of each round.
     * TODO: Implement "Backup Spawn" logic – when the enemy count drops below
     *       a configurable threshold, spawn a new enemy (e.g. triggered by a
     *       Wolf's Pack Howl) and add it to {@link #enemies}.
     * Example stub:
     * <pre>{@code
     * if (enemies.size() < BACKUP_THRESHOLD && backupPool.hasNext()) {
     *     Combatant backup = backupPool.next();
     *     enemies.add(backup);
     *     ui.displayBackupSpawn(backup.getName());
     * }
     * }</pre>
     */
    protected void triggerBackupSpawnIfNeeded() {
        // TODO: Backup Spawn logic goes here.
    }

    // -----------------------------------------------------------------------
    // Turn handling
    // -----------------------------------------------------------------------

    private void handlePlayerTurn(Combatant actor) {
        List<Combatant> availableTargets = enemies.stream()
                .filter(Combatant::isAlive)
                .collect(Collectors.toList());

        if (availableTargets.isEmpty()) return;

        Action action = ui.promptAction(actor, AVAILABLE_ACTIONS);

        Combatant target;
        if (action instanceof Defend) {
            // Defend targets self; pass a dummy target (unused by Defend.execute).
            target = actor;
        } else {
            target = ui.promptTarget(availableTargets);
        }

        action.execute(actor, target, ui);
    }

    private void handleAiTurn(Combatant actor) {
        List<Combatant> availableTargets = players.stream()
                .filter(Combatant::isAlive)
                .collect(Collectors.toList());

        if (availableTargets.isEmpty()) return;

        // Simple AI: 70% basic attack, 20% defend, 10% special skill.
        int roll = random.nextInt(100);
        Action action;
        if (roll < 70) {
            action = new BasicAttack();
        } else if (roll < 90) {
            action = new Defend();
        } else {
            action = new SpecialSkill();
        }

        Combatant target = availableTargets.get(random.nextInt(availableTargets.size()));

        if (action instanceof Defend) {
            action.execute(actor, actor, ui);
        } else {
            action.execute(actor, target, ui);
        }
    }

    // -----------------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------------

    private List<Combatant> getAllLiving() {
        List<Combatant> all = new ArrayList<>();
        players.stream().filter(Combatant::isAlive).forEach(all::add);
        enemies.stream().filter(Combatant::isAlive).forEach(all::add);
        return all;
    }

    private boolean isPlayer(Combatant c) {
        return players.contains(c);
    }

    private void displayAllStatuses() {
        ui.displayMessage("--- Players ---");
        players.forEach(ui::displayCombatantStatus);
        ui.displayMessage("--- Enemies ---");
        enemies.forEach(ui::displayCombatantStatus);
    }

    // -----------------------------------------------------------------------
    // Accessors (for testing)
    // -----------------------------------------------------------------------

    public List<Combatant> getPlayers() { return players; }
    public List<Combatant> getEnemies() { return enemies; }
    public int getCurrentRound()        { return currentRound; }
}
