package turnbasedcombat.domain;

import turnbasedcombat.domain.character.Combatant;
import turnbasedcombat.domain.character.Goblin;
import turnbasedcombat.domain.character.Wolf;
import java.util.ArrayList;
import java.util.List;

public class Level {
    private final int levelNumber;
    private String difficultyName; // Removed final
    private final List<Combatant> initialSpawns;
    private final List<Combatant> backupSpawns;
    private boolean backupTriggered;

    public Level(int levelNumber) {
        this.levelNumber = levelNumber;
        this.initialSpawns = new ArrayList<>();
        this.backupSpawns = new ArrayList<>();
        this.backupTriggered = false;
        setupEnemies();
    }

    private void setupEnemies() {
        if (levelNumber == 1) {
            this.difficultyName = "Easy";
            initialSpawns.add(new Goblin("Goblin A"));
            initialSpawns.add(new Goblin("Goblin B"));
            initialSpawns.add(new Goblin("Goblin C"));
        } else if (levelNumber == 2) {
            this.difficultyName = "Medium";
            initialSpawns.add(new Goblin("Goblin"));
            initialSpawns.add(new Wolf("Wolf"));
            backupSpawns.add(new Wolf("Wolf A"));
            backupSpawns.add(new Wolf("Wolf B"));
        } else if (levelNumber == 3) {
            this.difficultyName = "Hard";
            initialSpawns.add(new Goblin("Goblin A"));
            initialSpawns.add(new Goblin("Goblin B"));
            backupSpawns.add(new Goblin("Goblin Backup"));
            backupSpawns.add(new Wolf("Wolf A"));
            backupSpawns.add(new Wolf("Wolf B"));
        }
    }

    public List<Combatant> getInitialEnemies() { return new ArrayList<>(initialSpawns); }
    public List<Combatant> getBackupEnemies() { return new ArrayList<>(backupSpawns); }
    public boolean hasBackupSpawns() { return !backupSpawns.isEmpty(); }
    public boolean isBackupTriggered() { return backupTriggered; }
    public void setBackupTriggered(boolean triggered) { this.backupTriggered = triggered; }
    public String getDifficultyName() { return difficultyName; }
}