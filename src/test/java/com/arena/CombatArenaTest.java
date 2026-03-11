package com.arena;

import com.arena.action.BasicAttack;
import com.arena.action.Defend;
import com.arena.action.SpecialSkill;
import com.arena.core.Combatant;
import com.arena.engine.BattleEngine;
import com.arena.entities.Goblin;
import com.arena.entities.Warrior;
import com.arena.entities.Wizard;
import com.arena.entities.Wolf;
import com.arena.status.SmokeBombEffect;
import com.arena.status.StunEffect;
import com.arena.strategy.SpeedBasedTurnOrder;
import com.arena.ui.GameUI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for core game mechanics.
 */
class CombatArenaTest {

    private Warrior warrior;
    private Goblin goblin;
    private Wolf wolf;
    private Wizard wizard;

    @BeforeEach
    void setUp() {
        warrior = new Warrior("Aldric");
        wizard  = new Wizard("Seraphina");
        goblin  = new Goblin("Gruk");
        wolf    = new Wolf("Fang");
    }

    // -----------------------------------------------------------------------
    // Combatant basics
    // -----------------------------------------------------------------------

    @Test
    void combatantIsAliveWhenHpAboveZero() {
        assertTrue(warrior.isAlive());
    }

    @Test
    void combatantDiesWhenHpReachesZero() {
        // Warrior starts at 120 HP; deal more than that
        warrior.takeDamage(10_000);
        assertFalse(warrior.isAlive());
    }

    @Test
    void takeDamageRespectsDefense() {
        int hpBefore = goblin.getHp();
        // rawDamage 10, goblin defense 6 → actual damage max(1, 10-6) = 4
        goblin.takeDamage(10);
        assertEquals(hpBefore - 4, goblin.getHp());
    }

    @Test
    void takeDamageMinimumIsOne() {
        int hpBefore = goblin.getHp();
        goblin.takeDamage(1); // 1 - 6 defense = negative → clamped to 1
        assertEquals(hpBefore - 1, goblin.getHp());
    }

    @Test
    void healCapsAtMaxHp() {
        warrior.takeDamage(20);
        warrior.heal(1_000);
        assertEquals(warrior.getMaxHp(), warrior.getHp());
    }

    // -----------------------------------------------------------------------
    // Status effects
    // -----------------------------------------------------------------------

    @Test
    void stunEffectPreventsAction() {
        goblin.applyStatusEffect(new StunEffect(1));
        assertTrue(goblin.isStunned());
    }

    @Test
    void stunEffectClearsAfterExpiry() {
        goblin.applyStatusEffect(new StunEffect(1));
        goblin.tickStatusEffects(); // duration 1 → expires after one tick
        assertFalse(goblin.isStunned());
        assertTrue(goblin.getActiveEffects().isEmpty());
    }

    @Test
    void smokeBombEffectBlindsTarget() {
        warrior.applyStatusEffect(new SmokeBombEffect(2));
        assertTrue(warrior.isBlinded());
    }

    @Test
    void smokeBombClearsAfterExpiry() {
        warrior.applyStatusEffect(new SmokeBombEffect(2));
        warrior.tickStatusEffects(); // round 1 – still 1 remaining
        assertTrue(warrior.isBlinded());
        warrior.tickStatusEffects(); // round 2 – expires
        assertFalse(warrior.isBlinded());
    }

    // -----------------------------------------------------------------------
    // Actions
    // -----------------------------------------------------------------------

    @Test
    void basicAttackReducesTargetHp() {
        GameUI ui = silentUi();
        int hpBefore = goblin.getHp();
        new BasicAttack().execute(warrior, goblin, ui);
        assertTrue(goblin.getHp() < hpBefore);
    }

    @Test
    void defendDoublesEffectiveDefense() {
        GameUI ui = silentUi();
        new Defend().execute(warrior, warrior, ui);
        assertTrue(warrior.isDefending());

        int hpBefore = warrior.getHp();
        // Raw attack 12, warrior defense 12 doubled = 24 → max(1, 12-24) = 1
        warrior.takeDamage(goblin.getAttack());
        assertEquals(hpBefore - 1, warrior.getHp());
    }

    @Test
    void specialSkillDelegatesToCombatant() {
        GameUI ui = silentUi();
        int hpBefore = goblin.getHp();
        // Warrior's Shield Bash should deal extra damage to goblin
        new SpecialSkill().execute(warrior, goblin, ui);
        assertTrue(goblin.getHp() < hpBefore || goblin.isStunned());
    }

    // -----------------------------------------------------------------------
    // Strategy
    // -----------------------------------------------------------------------

    @Test
    void speedBasedOrderSortsBySpeedDescending() {
        List<Combatant> combatants = List.of(warrior, goblin, wolf);
        List<Combatant> ordered = new SpeedBasedTurnOrder().determineTurnOrder(combatants);
        // Wolf(18) > Goblin(16) > Warrior(8)
        assertEquals("Fang",    ordered.get(0).getName());
        assertEquals("Gruk",    ordered.get(1).getName());
        assertEquals("Aldric",  ordered.get(2).getName());
    }

    // -----------------------------------------------------------------------
    // BattleEngine win/loss
    // -----------------------------------------------------------------------

    @Test
    void battleEngineDetectsVictoryWhenAllEnemiesDead() {
        GameUI ui = silentUi();
        wolf.takeDamage(10_000);   // pre-kill enemies
        goblin.takeDamage(10_000);

        BattleEngine engine = new BattleEngine(
                List.of(warrior),
                List.of(goblin, wolf),
                new SpeedBasedTurnOrder(),
                ui
        );

        assertTrue(engine.isBattleOver());
        assertTrue(engine.isVictory());
    }

    @Test
    void battleEngineDetectsDefeatWhenAllPlayersDead() {
        GameUI ui = silentUi();
        warrior.takeDamage(10_000);
        wizard.takeDamage(10_000);

        BattleEngine engine = new BattleEngine(
                List.of(warrior, wizard),
                List.of(goblin),
                new SpeedBasedTurnOrder(),
                ui
        );

        assertTrue(engine.isBattleOver());
        assertFalse(engine.isVictory());
    }

    // -----------------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------------

    /** Returns a GameUI wired to /dev/null output and empty input. */
    private static GameUI silentUi() {
        PrintStream original = System.out;
        System.setOut(new PrintStream(new ByteArrayOutputStream()));
        try {
            return new GameUI();
        } finally {
            System.setOut(original);
        }
    }
}
