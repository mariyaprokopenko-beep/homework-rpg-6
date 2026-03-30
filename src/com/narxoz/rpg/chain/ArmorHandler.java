package com.narxoz.rpg.chain;

import com.narxoz.rpg.arena.ArenaFighter;

public class ArmorHandler extends DefenseHandler {
    private final int armorValue;

    public ArmorHandler(int armorValue) {
        this.armorValue = armorValue;
    }

    @Override
    public void handle(int incomingDamage, ArenaFighter target) {
        int reduced = incomingDamage - armorValue;
        if (reduced < 0) {
            reduced = 0;
        }
        int absorbed = incomingDamage - reduced;
        System.out.println("[Armor] Absorbed: " + absorbed + ", remaining damage: " + reduced);
        passToNext(reduced, target);
    }
}