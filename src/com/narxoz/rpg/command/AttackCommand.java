package com.narxoz.rpg.command;

import com.narxoz.rpg.arena.ArenaOpponent;

public class AttackCommand implements ActionCommand {
    private final ArenaOpponent target;
    private final int attackPower;
    private int damageDealt;

    public AttackCommand(ArenaOpponent target, int attackPower) {
        this.target = target;
        this.attackPower = attackPower;
    }

    @Override
    public void execute() {
        int currentHealth = target.getHealth();
        damageDealt = Math.min(attackPower, currentHealth);
        target.takeDamage(attackPower);
    }

    @Override
    public void undo() {
        target.restoreHealth(damageDealt);
    }

    @Override
    public String getDescription() {
        return "Attack for " + attackPower + " damage";
    }
}