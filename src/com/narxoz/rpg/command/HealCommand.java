package com.narxoz.rpg.command;

import com.narxoz.rpg.arena.ArenaFighter;

public class HealCommand implements ActionCommand {
    private final ArenaFighter target;
    private final int healAmount;
    private int actualHealApplied;

    public HealCommand(ArenaFighter target, int healAmount) {
        this.target = target;
        this.healAmount = healAmount;
    }

    @Override
    public void execute() {
        if (target.getHealPotions() <= 0) {
            actualHealApplied = 0;
            return;
        }
        int currentHealth = target.getHealth();
        int maxHealth = target.getMaxHealth();
        int space = maxHealth - currentHealth;
        actualHealApplied = Math.min(healAmount, space);
        target.heal(healAmount);
    }

    @Override
    public void undo() {
        if (actualHealApplied > 0) {
            target.takeDamage(actualHealApplied);
        }
    }

    @Override
    public String getDescription() {
        return "Heal for " + healAmount + " HP (potions left: " + target.getHealPotions() + ")";
    }
}