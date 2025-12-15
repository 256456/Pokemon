package com.pokemon;

import java.util.*;

public class Trainer {
    private String name;
    private List<Pokemon> party;
    private Pokemon activePokemon;

    public Trainer(String name) {
        this.name = name;
        this.party = new ArrayList<>();
    }

    public void addPokemonToParty(Pokemon pokemon) {
        if (party.size() < 6) {
            party.add(pokemon);
            if (activePokemon == null) {
                activePokemon = pokemon;
            }
        } else {
            System.out.println("队伍已满，无法添加更多宝可梦！");
        }
    }

    public boolean hasUsablePokemon() {
        for (Pokemon p : party) {
            if (!p.isFainted()) {
                return true;
            }
        }
        return false;
    }

    public void healAllPokemon() {
        for (Pokemon p : party) {
            p.healFull();
            p.restorePP();
        }
    }

    public void displayParty() {
        System.out.println("\n=== " + name + "的队伍 ===");
        for (int i = 0; i < party.size(); i++) {
            Pokemon p = party.get(i);
            String status = p.isFainted() ? "(濒死)" : "";
            System.out.println((i+1) + ". " + p + status);
            if (p == activePokemon) {
                System.out.println("   ↑ 当前出场");
            }
        }
    }

    // Getter和Setter方法
    public String getName() { return name; }
    public List<Pokemon> getParty() { return party; }
    public Pokemon getActivePokemon() { return activePokemon; }

    public void setActivePokemon(Pokemon pokemon) {
        if (party.contains(pokemon)) {
            activePokemon = pokemon;
        }
    }
}