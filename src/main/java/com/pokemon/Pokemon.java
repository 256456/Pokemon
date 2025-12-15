package com.pokemon;

import java.util.ArrayList;
import java.util.List;

public class Pokemon {
    private final String name;
    private int level;
    private List<Type> types;
    private int currentHP;
    private int maxHP;

    private List<Move> moves;
    private int attack;
    private int defense;
    private int specialAttack;
    private int specialDefense;
    private int speed;


    public Pokemon(String name, int level, Type[] types,
                   int hp, int attack, int defense,
                   int spAttack, int spDefense, int speed) {
        this.name = name;
        this.level = level;
        this.types = List.of(types);
        this.maxHP = hp;
        this.currentHP = hp;
        this.attack = attack;
        this.defense = defense;
        this.specialAttack = spAttack;
        this.specialDefense = spDefense;
        this.speed = speed;
        this.moves = new ArrayList<>();
    }

    public boolean takeDamage(int damage) {
        currentHP -= damage;
        if (currentHP < 0) currentHP = 0;
        return currentHP <= 0;
    }

    public void heal(int amount) {
        currentHP += amount;
        if (currentHP > maxHP) currentHP = maxHP;
    }

    public void healFull() {
        currentHP = maxHP;
    }

    public void restorePP() {
        for (Move move : moves) {
            move.restorePP();
        }
    }
    public void learnMove(Move move) {
        if (moves.size() < 4) {
            moves.add(move);
        } else {
            System.out.println(name + "已经学会了4个技能！");
        }
    }

    @Override
    public String toString() {
        return name + " Lv." + level + " HP:" + currentHP + "/" + maxHP;
    }

    public String getBattleInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" Lv.").append(level).append("\n");
        sb.append("HP: ").append(currentHP).append("/").append(maxHP).append("\n");
        sb.append("属性: ");
        for (Type type : types) {
            sb.append(type).append(" ");
        }
        return sb.toString();
    }

    // Getters
    public int getAttack() { return attack; }
    public int getSpecialAttack() { return specialAttack; }
    public int getLevel() { return level; }
    public int getDefense() { return defense; }
    public int getSpecialDefense() { return specialDefense; }
    public Type[] getTypes() { return types.toArray(new Type[0]); }
    public int getSpeed() { return speed; }
    public int getCurrentHP() { return currentHP; }
    public int getMaxHP() { return maxHP; }
    public String getName() { return name; }
    public List<Move> getMoves() { return moves; }

    public boolean isFainted() {return currentHP <= 0;
    }
}
