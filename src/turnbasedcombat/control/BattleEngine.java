package turnbasedcombat.control;

import turnbasedcombat.domain.Level;
import turnbasedcombat.domain.character.Combatant;
import turnbasedcombat.domain.action.Action;
import turnbasedcombat.domain.effect.StatusEffect;
import turnbasedcombat.domain.effect.StunEffect;
import turnbasedcombat.boundary.GameCLI;
import java.util.ArrayList;
import java.util.List;

public class BattleEngine {
    private int roundCount;
    private List<Combatant> playerTeam;
    private List<Combatant> enemyTeam;
    private TurnOrderStrategy turnStrategy;
    private GameCLI cli;
    private Level currentLevel;

    public BattleEngine(List<Combatant> playerTeam, Level level, TurnOrderStrategy turnStrategy, GameCLI cli) {
        this.playerTeam = new ArrayList<>(playerTeam);
        this.currentLevel = level;
        this.enemyTeam = currentLevel.getInitialEnemies(); // Load from Level
        this.turnStrategy = turnStrategy;
        this.cli = cli;
        this.roundCount = 0;
    }

    public boolean runBattle() {
        cli.displayBattleStart(playerTeam, enemyTeam);

        while (!checkGameEndingCondition()) {
            processRound();
        }

        boolean playerWon = isTeamAlive(playerTeam);
        // Note: You will need to make sure GameCLI has displayResults(playerWon, roundCount)
        cli.displayBattleEnd(playerWon); 
        return playerWon;
    }

    public void processRound() {
        roundCount++;
        cli.displayTurnStart(roundCount);

        List<Combatant> allCombatants = getAllCombatants();
        List<Combatant> turnOrder = turnStrategy.determineOrder(allCombatants);

        for (Combatant combatant : turnOrder) {
            if (!combatant.isAlive() || checkGameEndingCondition()) continue;

            // FIX: Apply status effects AND decrease cooldowns at the START of the turn
            applyStatusEffects(combatant);
            updateCoolDown(combatant); 

            if (isStunned(combatant)) {
                cli.displayStunned(combatant);
                continue;
            }

            executeTurnLogic(combatant, allCombatants);
        }
        
        triggerBackupSpawn();
        cli.displayTurnEnd(playerTeam, enemyTeam);
    }

    public void applyStatusEffects(Combatant c) {
        // Tick durations and remove expired ones before they act
        c.tickStatusEffects(); 
    }

    public void updateCoolDown(Combatant c) {
        // Decrease cooldown only if a turn took place (as per assignment rules)
        if (c.getSpecialCooldown() > 0) {
            c.setSpecialCooldown(c.getSpecialCooldown() - 1);
        }
    }

    public boolean checkGameEndingCondition() {
        // Game ends if players are dead OR (all enemies dead AND no backups left)
        boolean playersDead = !isTeamAlive(playerTeam);
        boolean enemiesDead = !isTeamAlive(enemyTeam) && (!currentLevel.hasBackupSpawns() || currentLevel.isBackupTriggered());
        return playersDead || enemiesDead;
    }

    public void triggerBackupSpawn() {
        if (!isTeamAlive(enemyTeam) && currentLevel.hasBackupSpawns() && !currentLevel.isBackupTriggered()) {
            cli.displayMessage("\nWARNING: Backup enemies have arrived!");
            enemyTeam.addAll(currentLevel.getBackupEnemies());
            currentLevel.setBackupTriggered(true);
        }
    }

    private void executeTurnLogic(Combatant combatant, List<Combatant> allCombatants) {
        cli.displayCombatantTurn(combatant);
        // The entity manages its own strategy or user input via the abstract takeTurn method
        combatant.takeTurn(this); 
    }

    private boolean isStunned(Combatant combatant) {
        for (StatusEffect effect : combatant.getStatusEffects()) {
            if (effect instanceof StunEffect && !effect.isExpired()) {
                return true;
            }
        }
        return false;
    }

    public List<Combatant> getAllCombatants() {
        List<Combatant> all = new ArrayList<>(playerTeam);
        all.addAll(enemyTeam);
        return all;
    }

    public List<Combatant> getAliveEnemies() {
        List<Combatant> alive = new ArrayList<>();
        for (Combatant c : enemyTeam) {
            if (c.isAlive()) alive.add(c);
        }
        return alive;
    }
    
    // Getters so Entities can access targets during takeTurn()
    public List<Combatant> getPlayerTeam() { return playerTeam; }
    public List<Combatant> getEnemyTeam() { return enemyTeam; }
    public GameCLI getCli() { return cli; }

    private boolean isTeamAlive(List<Combatant> team) {
        for (Combatant c : team) {
            if (c.isAlive()) return true;
        }
        return false;
    }
}