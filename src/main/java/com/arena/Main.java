package com.arena;

import com.arena.engine.BattleEngine;
import com.arena.entities.Goblin;
import com.arena.entities.Warrior;
import com.arena.entities.Wizard;
import com.arena.entities.Wolf;
import com.arena.strategy.SpeedBasedTurnOrder;
import com.arena.ui.GameUI;

import java.util.List;

/**
 * Application entry point.  Wires together all components and starts the
 * battle.
 *
 * <p>To run from the project root:
 * <pre>{@code
 *   mvn compile exec:java -Dexec.mainClass=com.arena.Main
 * }</pre>
 */
public class Main {

    public static void main(String[] args) {
        GameUI ui = new GameUI();

        // ----------------------------------------------------------------
        // Set up player party
        // ----------------------------------------------------------------
        Warrior warrior = new Warrior("Aldric");
        Wizard  wizard  = new Wizard("Seraphina");

        // ----------------------------------------------------------------
        // Set up enemy party
        // ----------------------------------------------------------------
        Goblin goblin = new Goblin("Gruk");
        Wolf   wolf   = new Wolf("Fang");

        // ----------------------------------------------------------------
        // Assemble and start the battle
        // ----------------------------------------------------------------
        BattleEngine engine = new BattleEngine(
                List.of(warrior, wizard),
                List.of(goblin, wolf),
                new SpeedBasedTurnOrder(),
                ui
        );

        engine.startBattle();
        ui.close();
    }
}
