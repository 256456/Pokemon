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


    private Status status;
    private int statusTurns;      // 状态持续回合数（用于睡眠、混乱）
    private int toxicCounter;     // 剧毒层数计数器
    private boolean isConfused;   // 是否混乱
    private int confusionTurns;   // 混乱剩余回合数
    private int originalSpeed;    // 记录原始速度（用于麻痹）
    private int originalAttack;   // 记录原始攻击（用于烧伤）


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

        this.status = Status.NONE;
        this.statusTurns = 0;
        this.toxicCounter = 1;
        this.isConfused = false;
        this.confusionTurns = 0;
        this.originalSpeed = speed;
        this.originalAttack = attack;
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


    public String setStatus(Status newStatus) {
        // 如果已经处于相同状态，返回null
        if (this.status == newStatus) {
            return null;
        }

        // 检查免疫
        if (isImmuneToStatus(newStatus)) {
            return name + " 对" + getStatusName(newStatus) + "免疫！";
        }

        // 清除原有状态的效果
        clearStatusEffects();

        this.status = newStatus;
        this.statusTurns = 0;
        this.toxicCounter = 1;

        // 初始化状态回合数
        String message = null;
        switch (newStatus) {
            case SLEEP:
                this.statusTurns = (int)(Math.random() * 3) + 1; // 1-3回合
                message = name + " 睡着了！将持续" + statusTurns + "回合";
                break;
            case CONFUSION:
                this.isConfused = true;
                this.confusionTurns = (int)(Math.random() * 3) + 2; // 2-4回合
                message = name + " 混乱了！将持续" + confusionTurns + "回合";
                break;
            case BADLY_POISON:
                this.toxicCounter = 1;
                message = name + " 中了剧毒！";
                break;
            case BURN:
                this.attack = originalAttack / 2; // 烧伤物理攻击减半
                message = name + " 烧伤了！物理攻击减半";
                break;
            case PARALYSIS:
                this.speed = originalSpeed / 2; // 麻痹速度减半
                message = name + " 麻痹了！速度减半";
                break;
            default:
                message = name + " 陷入了" + getStatusName(newStatus) + "状态！";
        }

        return message;
    }

    public void clearStatus() {
        if (this.status != Status.NONE) {
            System.out.println(name + " 的" + getStatusName(this.status) + "状态解除了！");
        }

        clearStatusEffects();
        this.status = Status.NONE;
        this.statusTurns = 0;
        this.toxicCounter = 1;
        this.isConfused = false;
        this.confusionTurns = 0;
    }
    // 清除状态带来的数值影响
    private void clearStatusEffects() {
        // 恢复烧伤降低的攻击
        if (this.status == Status.BURN) {
            this.attack = originalAttack;
        }
        // 恢复麻痹降低的速度
        if (this.status == Status.PARALYSIS) {
            this.speed = originalSpeed;
        }
    }

    // 检查状态免疫
    private boolean isImmuneToStatus(Status status) {
        return switch (status) {
            case FREEZE ->
                // 冰属性宝可梦不会冰冻
                    this.types.contains(Type.ICE);
            case POISON, BADLY_POISON ->
                // 毒属性和钢属性免疫中毒
                    this.types.contains(Type.POISON) || this.types.contains(Type.STEEL);
            default -> false;
        };
    }

    // 应用状态伤害（每回合调用）
    public void applyStatusDamage() {
        if (getCurrentHP()<=0 || status == Status.NONE) return;

        int damage = 0;
        switch (this.status) {
            case BURN:
            case POISON:
                damage = this.maxHP / 8;
                System.out.println(name + " 因" + getStatusName(status) + "损失了 " + damage + " HP！");
                break;
            case BADLY_POISON:
                damage = (this.maxHP * toxicCounter) / 16;
                System.out.println(name + " 因剧毒损失了 " + damage + " HP！（层数：" + toxicCounter + "）");
                toxicCounter = Math.min(toxicCounter + 1, 15); // 增加层数，最多15层
                break;
        }

        if (damage > 0) {
            takeDamage(damage);
        }
    }

    // 检查当前回合是否能行动
    public boolean canAttackThisTurn() {
        // 检查异常状态
        switch (this.status) {
            case FREEZE:
                // 20%几率解除
                if (Math.random() < 0.2) {
                    System.out.println(name + " 的冰冻解除了！");
                    clearStatus();
                    return true;
                }
                System.out.println(name + " 被冰冻了，无法行动！");
                return false;

            case PARALYSIS:
                // 25%几率无法行动
                if (Math.random() < 0.25) {
                    System.out.println(name + " 因麻痹而无法行动！");
                    return false;
                }
                return true;

            case SLEEP:
                if (statusTurns > 0) {
                    statusTurns--;
                    System.out.println(name + " 正在沉睡...（剩余" + statusTurns + "回合）");
                    if (statusTurns == 0) {
                        clearStatus();
                        System.out.println(name + " 醒来了！");
                    }
                    return false;
                }
                break;
        }

        // 检查混乱
        if (isConfused && confusionTurns > 0) {
            confusionTurns--;
            if (confusionTurns == 0) {
                isConfused = false;
                System.out.println(name + " 从混乱中恢复了！");
                return true;
            }

            // 33%几率攻击自己
            if (Math.random() < 0.33) {
                System.out.println(name + " 因为混乱而攻击了自己！");
                int damage = 40; // 基础混乱伤害
                takeDamage(damage);
                System.out.println("造成了 " + damage + " 点伤害！");
                return false; // 混乱自伤后本回合不攻击
            }
        }

        return true;
    }

    public static String getStatusName(Status status) {
        switch (status) {
            case BURN: return "烧伤";
            case FREEZE: return "冰冻";
            case PARALYSIS: return "麻痹";
            case POISON: return "中毒";
            case BADLY_POISON: return "剧毒";
            case SLEEP: return "睡眠";
            case CONFUSION: return "混乱";
            default: return "无";
        }
    }

    public String getStatusString() {
        if (status == Status.NONE && !isConfused) return "";

        StringBuilder sb = new StringBuilder();
        if (status != Status.NONE) {
            sb.append(getStatusName(status));
        }
        if (isConfused) {
            if (sb.length() > 0) sb.append(", ");
            sb.append("混乱");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return name + " Lv." + level + " HP:" + currentHP + "/" + maxHP + " (属性: " +
                Type.getTypesString(types.toArray(Type[]::new)) + ")";
    }


    // Getters

    public Status getStatus() { return status; }
    public boolean isConfused() { return isConfused; }
    public int getStatusTurns() { return statusTurns; }

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
