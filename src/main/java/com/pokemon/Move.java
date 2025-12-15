package com.pokemon;

public class Move {
    private String name;
    private Type type;
    private Category category; // 物理/特殊/变化
    private int power;
    private int accuracy;
    private int maxPP;
    private int currentPP;


    private Status statusEffect;
    private int statusChance; // 状态触发几率（百分比）
    private String effectDescription;

    public enum Category {
        PHYSICAL, SPECIAL, STATUS
    }

    public void restorePP() {
        currentPP = maxPP;
    }

    public Move(String name, Type type, Category category,
                int power, int accuracy, int pp,
                Status statusEffect, int statusChance) {
        this.name = name;
        this.type = type;
        this.category = category;
        this.power = power;
        this.accuracy = accuracy;
        this.maxPP = pp;
        this.currentPP = pp;
        this.statusEffect = statusEffect;
        this.statusChance = statusChance;

        // 设置效果描述
        if (statusEffect != Status.NONE && statusChance > 0) {
            this.effectDescription = String.format("有%d%%几率使对手%s",
                    statusChance, Pokemon.getStatusName(statusEffect));
        }
    }

    public MoveResult use(Pokemon attacker, Pokemon defender) {
        if (currentPP <= 0) {
            return new MoveResult(false, false, false, 0, null);
        }

        currentPP--;

        // 检查命中
        if (!hitCheck()) {
            return new MoveResult(false, false, false, 0, null);
        }

        boolean hit = true;
        boolean inflictedStatus = false;
        String statusMessage = null;

        // 状态技能
        if (category == Category.STATUS) {
            if (statusEffect != Status.NONE) {
                statusMessage = attemptInflictStatus(defender);
                inflictedStatus = statusMessage != null;
            }
            return new MoveResult(hit, false, inflictedStatus, 0, statusMessage);
        }

        // 攻击技能
        boolean isCritical = checkCriticalHit();
        int damage = calculateDamage(attacker, defender, isCritical);

        // 造成伤害
        boolean fainted = defender.takeDamage(damage);

        // 尝试附加状态效果
        if (statusEffect != Status.NONE && !fainted) {
            statusMessage = attemptInflictStatus(defender);
            inflictedStatus = statusMessage != null;
        }

        return new MoveResult(hit, fainted, inflictedStatus, damage, statusMessage);
    }
    // 尝试附加状态
    private String attemptInflictStatus(Pokemon target) {
        if (statusEffect == Status.NONE || statusChance <= 0) return null;
        if (Math.random() * 100 < statusChance) return target.setStatus(statusEffect);
        return null;
    }

    public boolean hitCheck() {return Math.random() * 100 < accuracy;}

    public void use() {currentPP--;}

    public int calculateDamage(Pokemon attacker, Pokemon defender, boolean isCritical) {
        if (category == Category.STATUS) return 0;

        int attackStat = category == Category.PHYSICAL ?
                attacker.getAttack() : attacker.getSpecialAttack();
        int defenseStat = category == Category.PHYSICAL ?
                defender.getDefense() : defender.getSpecialDefense();

        double damage = ((2 * attacker.getLevel()+10)/250.0 * power*attackStat / defenseStat) + 2;

        double typeEffectiveness = calculateTypeEffectiveness(attacker,defender);
        damage *= typeEffectiveness* (0.85 + Math.random() * 0.15);

        if (isCritical) damage *= 1.5;

        if (typeEffectiveness > 0 && damage < 1) damage = 1;

        return (int)damage;
    }

    public boolean checkCriticalHit() {
        return Math.random() < (1.0 / 16.0);
    }

    private double calculateTypeEffectiveness(Pokemon attacker,Pokemon defender) {
        double multiplier = 1.0;

        for (Type attackerType : attacker.getTypes()){//属性一致加成
            if (this.type.equals(attackerType)) multiplier*=1.5;
        }

        for (Type defenderType : defender.getTypes()) {//属性相克
            multiplier *= Type.getEffectiveness(this.type, defenderType);
        }
        return multiplier;
    }


    @Override
    public String toString() {
        return String.format("%-10s %-8s 威力:%-4d 命中:%-3d%% PP:%2d/%-2d",
                name, type, power, accuracy, currentPP, maxPP);
    }

    public String getName() { return name; }
    public Type getType() { return type; }
    public Category getCategory() { return category; }
    public int getPower() { return power; }
    public int getAccuracy() { return accuracy; }
    public int getCurrentPP() { return currentPP; }
    public int getMaxPP() { return maxPP; }
    public String getEffectDescription() { return effectDescription; }

    public static class MoveResult {
        public final boolean hit;
        public final boolean fainted;
        public final boolean inflictedStatus;
        public final int damage;
        public final String statusMessage;

        public MoveResult(boolean hit, boolean fainted, boolean inflictedStatus, int damage, String statusMessage) {
            this.hit = hit;
            this.fainted = fainted;
            this.inflictedStatus = inflictedStatus;
            this.damage = damage;
            this.statusMessage = statusMessage;
        }
    }
}

