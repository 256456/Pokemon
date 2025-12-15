package com.pokemon;

public enum Type {
    NORMAL, FIRE, WATER, GRASS, ELECTRIC, ICE, FIGHTING, POISON, GROUND,
    FLYING, PSYCHIC, BUG, ROCK, GHOST, DRAGON, DARK, STEEL, FAIRY;

    private static final double[][] effectivenessTable;

    static {
        int size = values().length;
        effectivenessTable = new double[size][size];

        // 初始化所有为1.0
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                effectivenessTable[i][j] = 1.0;
            }
        }

        // 设置属性相克 (完整版)

        // 普通系
        setEffectiveness(NORMAL, ROCK, 0.5);
        setEffectiveness(NORMAL, STEEL, 0.5);
        setEffectiveness(NORMAL, GHOST, 0.0);

        // 火系
        setEffectiveness(FIRE, GRASS, 2.0);
        setEffectiveness(FIRE, ICE, 2.0);
        setEffectiveness(FIRE, BUG, 2.0);
        setEffectiveness(FIRE, STEEL, 2.0);
        setEffectiveness(FIRE, WATER, 0.5);
        setEffectiveness(FIRE, ROCK, 0.5);
        setEffectiveness(FIRE, FIRE, 0.5);
        setEffectiveness(FIRE, DRAGON, 0.5);

        // 水系
        setEffectiveness(WATER, FIRE, 2.0);
        setEffectiveness(WATER, GROUND, 2.0);
        setEffectiveness(WATER, ROCK, 2.0);
        setEffectiveness(WATER, WATER, 0.5);
        setEffectiveness(WATER, GRASS, 0.5);
        setEffectiveness(WATER, DRAGON, 0.5);

        // 草系
        setEffectiveness(GRASS, WATER, 2.0);
        setEffectiveness(GRASS, GROUND, 2.0);
        setEffectiveness(GRASS, ROCK, 2.0);
        setEffectiveness(GRASS, FIRE, 0.5);
        setEffectiveness(GRASS, GRASS, 0.5);
        setEffectiveness(GRASS, POISON, 0.5);
        setEffectiveness(GRASS, FLYING, 0.5);
        setEffectiveness(GRASS, BUG, 0.5);
        setEffectiveness(GRASS, DRAGON, 0.5);
        setEffectiveness(GRASS, STEEL, 0.5);

        // 电系
        setEffectiveness(ELECTRIC, WATER, 2.0);
        setEffectiveness(ELECTRIC, FLYING, 2.0);
        setEffectiveness(ELECTRIC, GROUND, 0.0);
        setEffectiveness(ELECTRIC, GRASS, 0.5);
        setEffectiveness(ELECTRIC, ELECTRIC, 0.5);
        setEffectiveness(ELECTRIC, DRAGON, 0.5);

        // 冰系
        setEffectiveness(ICE, GRASS, 2.0);
        setEffectiveness(ICE, GROUND, 2.0);
        setEffectiveness(ICE, FLYING, 2.0);
        setEffectiveness(ICE, DRAGON, 2.0);
        setEffectiveness(ICE, FIRE, 0.5);
        setEffectiveness(ICE, WATER, 0.5);
        setEffectiveness(ICE, ICE, 0.5);
        setEffectiveness(ICE, STEEL, 0.5);

        // 格斗系
        setEffectiveness(FIGHTING, NORMAL, 2.0);
        setEffectiveness(FIGHTING, ICE, 2.0);
        setEffectiveness(FIGHTING, ROCK, 2.0);
        setEffectiveness(FIGHTING, DARK, 2.0);
        setEffectiveness(FIGHTING, STEEL, 2.0);
        setEffectiveness(FIGHTING, POISON, 0.5);
        setEffectiveness(FIGHTING, FLYING, 0.5);
        setEffectiveness(FIGHTING, PSYCHIC, 0.5);
        setEffectiveness(FIGHTING, BUG, 0.5);
        setEffectiveness(FIGHTING, FAIRY, 0.5);
        setEffectiveness(FIGHTING, GHOST, 0.0);

        // 毒系
        setEffectiveness(POISON, GRASS, 2.0);
        setEffectiveness(POISON, FAIRY, 2.0);
        setEffectiveness(POISON, POISON, 0.5);
        setEffectiveness(POISON, GROUND, 0.5);
        setEffectiveness(POISON, ROCK, 0.5);
        setEffectiveness(POISON, GHOST, 0.5);
        setEffectiveness(POISON, STEEL, 0.0);

        // 地面系
        setEffectiveness(GROUND, FIRE, 2.0);
        setEffectiveness(GROUND, ELECTRIC, 2.0);
        setEffectiveness(GROUND, POISON, 2.0);
        setEffectiveness(GROUND, ROCK, 2.0);
        setEffectiveness(GROUND, STEEL, 2.0);
        setEffectiveness(GROUND, GRASS, 0.5);
        setEffectiveness(GROUND, BUG, 0.5);
        setEffectiveness(GROUND, FLYING, 0.0);

        // 飞行系
        setEffectiveness(FLYING, GRASS, 2.0);
        setEffectiveness(FLYING, FIGHTING, 2.0);
        setEffectiveness(FLYING, BUG, 2.0);
        setEffectiveness(FLYING, ELECTRIC, 0.5);
        setEffectiveness(FLYING, ROCK, 0.5);
        setEffectiveness(FLYING, STEEL, 0.5);

        // 超能力系
        setEffectiveness(PSYCHIC, FIGHTING, 2.0);
        setEffectiveness(PSYCHIC, POISON, 2.0);
        setEffectiveness(PSYCHIC, PSYCHIC, 0.5);
        setEffectiveness(PSYCHIC, STEEL, 0.5);
        setEffectiveness(PSYCHIC, DARK, 0.0);

        // 虫系
        setEffectiveness(BUG, GRASS, 2.0);
        setEffectiveness(BUG, PSYCHIC, 2.0);
        setEffectiveness(BUG, DARK, 2.0);
        setEffectiveness(BUG, FIRE, 0.5);
        setEffectiveness(BUG, FIGHTING, 0.5);
        setEffectiveness(BUG, POISON, 0.5);
        setEffectiveness(BUG, FLYING, 0.5);
        setEffectiveness(BUG, GHOST, 0.5);
        setEffectiveness(BUG, STEEL, 0.5);
        setEffectiveness(BUG, FAIRY, 0.5);

        // 岩石系
        setEffectiveness(ROCK, FIRE, 2.0);
        setEffectiveness(ROCK, ICE, 2.0);
        setEffectiveness(ROCK, FLYING, 2.0);
        setEffectiveness(ROCK, BUG, 2.0);
        setEffectiveness(ROCK, FIGHTING, 0.5);
        setEffectiveness(ROCK, GROUND, 0.5);
        setEffectiveness(ROCK, STEEL, 0.5);

        // 幽灵系
        setEffectiveness(GHOST, PSYCHIC, 2.0);
        setEffectiveness(GHOST, GHOST, 2.0);
        setEffectiveness(GHOST, DARK, 0.5);
        setEffectiveness(GHOST, NORMAL, 0.0);

        // 龙系
        setEffectiveness(DRAGON, DRAGON, 2.0);
        setEffectiveness(DRAGON, STEEL, 0.5);
        setEffectiveness(DRAGON, FAIRY, 0.0);

        // 恶系
        setEffectiveness(DARK, PSYCHIC, 2.0);
        setEffectiveness(DARK, GHOST, 2.0);
        setEffectiveness(DARK, FIGHTING, 0.5);
        setEffectiveness(DARK, DARK, 0.5);
        setEffectiveness(DARK, FAIRY, 0.5);

        // 钢系
        setEffectiveness(STEEL, ICE, 2.0);
        setEffectiveness(STEEL, ROCK, 2.0);
        setEffectiveness(STEEL, FAIRY, 2.0);
        setEffectiveness(STEEL, FIRE, 0.5);
        setEffectiveness(STEEL, WATER, 0.5);
        setEffectiveness(STEEL, ELECTRIC, 0.5);
        setEffectiveness(STEEL, STEEL, 0.5);

        // 妖精系
        setEffectiveness(FAIRY, FIGHTING, 2.0);
        setEffectiveness(FAIRY, DRAGON, 2.0);
        setEffectiveness(FAIRY, DARK, 2.0);
        setEffectiveness(FAIRY, FIRE, 0.5);
        setEffectiveness(FAIRY, POISON, 0.5);
        setEffectiveness(FAIRY, STEEL, 0.5);
    }
    private static void setEffectiveness(Type attack, Type defense, double multiplier) {
        effectivenessTable[attack.ordinal()][defense.ordinal()] = multiplier;
    }

    public static double getEffectiveness(Type attack, Type defense) {
        return effectivenessTable[attack.ordinal()][defense.ordinal()];
    }

    public static String getTypeEffectivenessMessage(double effectiveness) {
        if (effectiveness == 0.0) {
            return "没有效果...";
        } else if (effectiveness > 1.0) {
            return "效果拔群！";
        } else if (effectiveness < 1.0) {
            return "效果不理想...";
        } else {
            return "";
        }
    }
}