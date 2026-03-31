package com.narxoz.rpg;

import com.narxoz.rpg.arena.ArenaFighter;
import com.narxoz.rpg.arena.ArenaOpponent;
import com.narxoz.rpg.arena.TournamentResult;
import com.narxoz.rpg.chain.ArmorHandler;
import com.narxoz.rpg.chain.BlockHandler;
import com.narxoz.rpg.chain.DefenseHandler;
import com.narxoz.rpg.chain.DodgeHandler;
import com.narxoz.rpg.chain.HpHandler;
import com.narxoz.rpg.command.ActionQueue;
import com.narxoz.rpg.command.AttackCommand;
import com.narxoz.rpg.command.DefendCommand;
import com.narxoz.rpg.command.HealCommand;
import com.narxoz.rpg.tournament.TournamentEngine;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Homework 6 Demo: Chain of Responsibility + Command ===\n");

        ArenaFighter hero = new ArenaFighter("Sir Roland", 120, 0.25, 30, 8, 22, 3);
        ArenaOpponent opponent = new ArenaOpponent("Black Knight", 110, 18);

        // -----------------------------------------------------------------------
        // Part 1 — Command Queue Demo
        // -----------------------------------------------------------------------
        System.out.println("--- Command Queue Demo ---");

        ActionQueue queue = new ActionQueue();

        queue.enqueue(new AttackCommand(opponent, hero.getAttackPower()));
        queue.enqueue(new HealCommand(hero, 20));
        queue.enqueue(new DefendCommand(hero, 0.15));

        System.out.println("Queued actions:");
        for (String desc : queue.getCommandDescriptions()) {
            System.out.println("  " + desc);
        }

        System.out.println("\nUndoing last queued action...");
        queue.undoLast();

        System.out.println("Queue after undo:");
        for (String desc : queue.getCommandDescriptions()) {
            System.out.println("  " + desc);
        }

        queue.enqueue(new DefendCommand(hero, 0.15));
        System.out.println("\nExecuting all queued commands...");
        queue.executeAll();

        // -----------------------------------------------------------------------
        // Part 2 — Defense Chain Demo
        // -----------------------------------------------------------------------
        System.out.println("\n--- Defense Chain Demo ---");

        DefenseHandler dodge = new DodgeHandler(0.50, 99L);
        DefenseHandler block = new BlockHandler(0.30);
        DefenseHandler armor = new ArmorHandler(5);
        DefenseHandler hp = new HpHandler();
        dodge.setNext(block).setNext(armor).setNext(hp);

        ArenaFighter testFighter = new ArenaFighter("Test", 100, 0.20, 25, 5, 18, 3);
        System.out.println("Sending 20 incoming damage through the defense chain...");
        System.out.println("Hero HP before: " + testFighter.getHealth());
        dodge.handle(20, testFighter);
        System.out.println("Hero HP after:  " + testFighter.getHealth());

        // -----------------------------------------------------------------------
        // Part 3 — Full Tournament Demo
        // -----------------------------------------------------------------------
        System.out.println("\n--- Full Arena Tournament ---");

        ArenaFighter tournamentHero = new ArenaFighter("Erlan the Brave", 130, 0.25, 30, 10, 24, 4);
        ArenaOpponent tournamentOpponent = new ArenaOpponent("Iron Vane", 120, 18);

        TournamentResult result = new TournamentEngine(tournamentHero, tournamentOpponent)
                .setRandomSeed(42L)
                .runTournament();

        System.out.println("Winner : " + result.getWinner());
        System.out.println("Rounds : " + result.getRounds());
        System.out.println("Battle log:");
        for (String line : result.getLog()) {
            System.out.println("  " + line);
        }

        System.out.println("\n=== Demo Complete ===");
    }
}