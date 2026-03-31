package com.narxoz.rpg.tournament;

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
import java.util.Random;

public class TournamentEngine {
    private final ArenaFighter hero;
    private final ArenaOpponent opponent;
    private Random random = new Random(1L);

    public TournamentEngine(ArenaFighter hero, ArenaOpponent opponent) {
        this.hero = hero;
        this.opponent = opponent;
    }

    public TournamentEngine setRandomSeed(long seed) {
        this.random = new Random(seed);
        return this;
    }

    public TournamentResult runTournament() {
        TournamentResult result = new TournamentResult();
        int round = 0;
        final int maxRounds = 20;

        DefenseHandler dodge = new DodgeHandler(hero.getDodgeChance(), random.nextLong());
        DefenseHandler block = new BlockHandler(hero.getBlockRating() / 100.0);
        DefenseHandler armor = new ArmorHandler(hero.getArmorValue());
        DefenseHandler hp = new HpHandler();
        dodge.setNext(block).setNext(armor).setNext(hp);
        DefenseHandler defenseChain = dodge;

        ActionQueue actionQueue = new ActionQueue();

        result.addLine("=== TOURNAMENT START ===");
        result.addLine(hero.getName() + " vs " + opponent.getName());

        while (hero.isAlive() && opponent.isAlive() && round < maxRounds) {
            round++;
            result.addLine("\n--- ROUND " + round + " ---");

            actionQueue.enqueue(new AttackCommand(opponent, hero.getAttackPower()));
            if (hero.getHealPotions() > 0) {
                actionQueue.enqueue(new HealCommand(hero, 20));
            }
            actionQueue.enqueue(new DefendCommand(hero, 0.10));

            result.addLine("Queued actions: " + actionQueue.getCommandDescriptions());

            actionQueue.executeAll();

            result.addLine("Hero HP: " + hero.getHealth() + ", Opponent HP: " + opponent.getHealth());

            if (!opponent.isAlive()) {
                result.addLine(opponent.getName() + " has been defeated!");
                break;
            }

            int opponentDamage = opponent.getAttackPower();
            result.addLine(opponent.getName() + " attacks for " + opponentDamage + " damage!");
            defenseChain.handle(opponentDamage, hero);

            result.addLine("After defense: Hero HP = " + hero.getHealth());

            if (!hero.isAlive()) {
                result.addLine(hero.getName() + " has been defeated!");
                break;
            }
        }

        result.setRounds(round);
        if (hero.isAlive() && opponent.isAlive()) {
            result.setWinner("Draw (max rounds reached)");
        } else if (hero.isAlive()) {
            result.setWinner(hero.getName());
        } else {
            result.setWinner(opponent.getName());
        }
        result.addLine("\nTournament winner: " + result.getWinner());

        return result;
    }
}