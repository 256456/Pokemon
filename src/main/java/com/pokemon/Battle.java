package com.pokemon;

import java.util.*;

public class Battle {
    Trainer player;
    Trainer opponent;
    private Scanner scanner;
    private boolean isBattleOver;
    private int turnCount;

    public Battle(Trainer player, Trainer opponent) {
        this.player = player;
        this.opponent = opponent;
        this.scanner = new Scanner(System.in);
        this.isBattleOver = false;
        this.turnCount = 0;
    }

    public void executeTurn() {
        turnCount++;
        System.out.println("\n=== 第 " + turnCount + " 回合 ===");

        // 检查宝可梦状态
        checkPokemonStatus();
        if (isBattleOver) return;

        // 显示对战状态
        displayBattleStatus();

        int playerAction = getPlayerAction();
        int opponentAction = getAIAction();

        determineAndExecuteActions(playerAction, opponentAction);

        checkBattleEnd();
    }

    private void checkPokemonStatus() {
        // 检查玩家宝可梦
        if (player.getActivePokemon().isFainted()) {
            System.out.println(player.getActivePokemon().getName() + "倒下了！");
            if (player.hasUsablePokemon()) {
                playerSwitchPokemon();
            } else {
                isBattleOver = true;
                return;
            }
        }

        // 检查对手宝可梦
        if (opponent.getActivePokemon().isFainted()) {
            System.out.println(opponent.getActivePokemon().getName() + "倒下了！");
            if (opponent.hasUsablePokemon()) {
                opponentSwitchPokemonAI();
            } else {
                isBattleOver = true;
                return;
            }
        }
    }

    private void displayBattleStatus() {
        Pokemon playerPokemon = player.getActivePokemon();
        Pokemon opponentPokemon = opponent.getActivePokemon();

        System.out.println("\n" + opponent.getName() + "的 " + opponentPokemon.getName());
        System.out.println("HP: [" + getHealthBar(opponentPokemon) + "] " +
                opponentPokemon.getCurrentHP() + "/" + opponentPokemon.getMaxHP());
        System.out.println("属性: " + getTypesString(opponentPokemon));

        System.out.println("\nVS\n");

        System.out.println(player.getName() + "的 " + playerPokemon.getName());
        System.out.println("HP: [" + getHealthBar(playerPokemon) + "] " +
                playerPokemon.getCurrentHP() + "/" + playerPokemon.getMaxHP());
        System.out.println("属性: " + getTypesString(playerPokemon));
    }

    private String getHealthBar(Pokemon pokemon) {
        int maxBars = 20;
        int currentBars = (int)((double)pokemon.getCurrentHP() / pokemon.getMaxHP() * maxBars);
        StringBuilder bar = new StringBuilder();

        for (int i = 0; i < maxBars; i++) {
            if (i < currentBars) {
                bar.append("█");
            } else {
                bar.append("░");
            }
        }

        return bar.toString();
    }

    private String getTypesString(Pokemon pokemon) {
        StringBuilder typesStr = new StringBuilder();
        for (Type type : pokemon.getTypes()) {
            typesStr.append(type).append(" ");
        }
        return typesStr.toString().trim();
    }

    private int getPlayerAction() {
        while (true) {
            System.out.println("\n选择行动:");
            System.out.println("1. 攻击");
            System.out.println("2. 切换宝可梦");
            System.out.println("3. 查看宝可梦信息");
            System.out.println("4. 逃跑");
            System.out.print("请输入选择 (1-4): ");

            try {
                int choice = scanner.nextInt();
                if (choice >= 1 && choice <= 4) {
                    return choice;
                }
                System.out.println("无效的选择！请输入1-4之间的数字。");
            } catch (Exception e) {
                scanner.nextLine(); // 清除无效输入
                System.out.println("请输入有效数字！");
            }
        }
    }

    private int getAIAction() {
        return 1;
    }

    private void determineAndExecuteActions(int playerAction, int opponentAction) {
        Pokemon playerPokemon = player.getActivePokemon();
        Pokemon opponentPokemon = opponent.getActivePokemon();

        // 判断速度决定行动顺序
        int playerSpeed = playerPokemon.getSpeed();
        int opponentSpeed = opponentPokemon.getSpeed();

        boolean playerFirst = (playerSpeed >= opponentSpeed);

        // 执行行动
        if (playerFirst) {
            executePlayerAction(playerAction);
            if (!isBattleOver && !opponentPokemon.isFainted()) {
                executeOpponentAction(opponentAction);
            }
        } else {
            executeOpponentAction(opponentAction);
            if (!isBattleOver && !playerPokemon.isFainted()) {
                executePlayerAction(playerAction);
            }
        }
    }

    private void executePlayerAction(int action) {
        switch (action) {
            case 1:
                executePlayerAttack();
                break;
            case 2:
                playerSwitchPokemon();
                break;
            case 3:
                viewPokemonInfo();
                executePlayerAction(getPlayerAction()); // 重新选择行动
                break;
            case 4:
                attemptEscape();
                break;
        }
    }

    private void executeOpponentAction(int action) {
        switch (action) {
            case 1:
                executeOpponentAttack();
                break;
            case 2:
                opponentSwitchPokemonAI();
                break;
        }
    }

    private void executePlayerAttack() {
        Pokemon attacker = player.getActivePokemon();
        Pokemon defender = opponent.getActivePokemon();

        Move selectedMove = selectPlayerMove(attacker);
        if (selectedMove == null) return;

        executeAttack(attacker, defender, selectedMove, player.getName());
    }

    private void executeOpponentAttack() {
        Pokemon attacker = opponent.getActivePokemon();
        Pokemon defender = player.getActivePokemon();

        Move selectedMove = selectAIMove(attacker);
        if (selectedMove == null) return;

        executeAttack(attacker, defender, selectedMove, opponent.getName());
    }

    private Move selectPlayerMove(Pokemon pokemon) {
        List<Move> moves = pokemon.getMoves();
        if (moves.isEmpty()) {
            System.out.println(pokemon.getName() + "没有可以使用的技能！");
            return null;
        }

        System.out.println("\n选择技能:");
        for (int i = 0; i < moves.size(); i++) {
            System.out.println((i+1) + ". " + moves.get(i));
        }

        while (true) {
            System.out.print("请输入选择 (1-" + moves.size() + "): ");
            try {
                int choice = scanner.nextInt();
                if (choice >= 1 && choice <= moves.size()) {
                    Move selected = moves.get(choice - 1);
                    if (selected.getCurrentPP() <= 0) {
                        System.out.println("这个技能的PP耗尽了！");
                        continue;
                    }
                    return selected;
                }
                System.out.println("无效的选择！");
            } catch (Exception e) {
                scanner.nextLine();
                System.out.println("请输入有效数字！");
            }
        }
    }

    private Move selectAIMove(Pokemon pokemon) {
        List<Move> moves = pokemon.getMoves();
        List<Move> usableMoves = new ArrayList<>();

        // 找出PP不为0的技能
        for (Move move : moves) {
            if (move.getCurrentPP() > 0) {
                usableMoves.add(move);
            }
        }

        if (usableMoves.isEmpty()) {
            System.out.println(pokemon.getName() + "没有可以使用的技能！");
            return null;
        }

        // AI选择：优先选择克制对方的技能
        Pokemon target = player.getActivePokemon();
        Move bestMove = null;
        double bestEffectiveness = 0;

        for (Move move : usableMoves) {
            double effectiveness = 1.0;
            for (Type targetType : target.getTypes()) {
                effectiveness *= Type.getEffectiveness(move.getType(), targetType);
            }

            if (effectiveness > bestEffectiveness) {
                bestEffectiveness = effectiveness;
                bestMove = move;
            }
        }

        // 如果没有克制技能，随机选择一个
        if (bestMove == null || bestEffectiveness <= 1.0) {
            int randomIndex = (int)(Math.random() * usableMoves.size());
            bestMove = usableMoves.get(randomIndex);
        }

        return bestMove;
    }

    private void executeAttack(Pokemon attacker, Pokemon defender, Move move, String trainerName) {
        // 检查PP
        if (move.getCurrentPP() <= 0) {
            System.out.println(attacker.getName() + "的" + move.getName() + "PP耗尽了！");
            return;
        }

        // 使用技能
        move.use();

        // 检查命中
        if (!move.hitCheck()) {
            System.out.println(trainerName + "的" + attacker.getName() + "的" + move.getName() + "没有命中！");
            return;
        }

        // 检查要害
        boolean isCritical = move.checkCriticalHit();

        // 计算伤害
        int damage = move.calculateDamage(attacker, defender, isCritical);

        // 显示攻击信息
        System.out.print(trainerName + "的" + attacker.getName() + "使用了" + move.getName() + "！");

        if (isCritical) {
            System.out.print(" 击中要害！");
        }

        // 检查属性相克
        double effectiveness = 1.0;
        for (Type defenderType : defender.getTypes()) {
            effectiveness *= Type.getEffectiveness(move.getType(), defenderType);
        }

        String effectivenessMsg = Type.getTypeEffectivenessMessage(effectiveness);
        if (!effectivenessMsg.isEmpty()) {
            System.out.print(" " + effectivenessMsg);
        }
        System.out.println();

        // 造成伤害（如果有效）
        if (effectiveness > 0) {
            boolean fainted = defender.takeDamage(damage);
            System.out.println("对" + defender.getName() + "造成了" + damage + "点伤害！");

            if (fainted) {
                System.out.println(defender.getName() + "倒下了！");
            }
        }
    }

    private void playerSwitchPokemon() {
        List<Pokemon> party = player.getParty();
        List<Pokemon> usablePokemon = new ArrayList<>();

        System.out.println("\n选择要上场的宝可梦:");
        for (int i = 0; i < party.size(); i++) {
            Pokemon p = party.get(i);
            if (p != player.getActivePokemon() && !p.isFainted()) {
                usablePokemon.add(p);
                System.out.println(usablePokemon.size() + ". " + p);
            }
        }

        if (usablePokemon.isEmpty()) {
            System.out.println("没有可以替换的宝可梦！");
            return;
        }

        System.out.println((usablePokemon.size() + 1) + ". 取消");

        while (true) {
            System.out.print("请输入选择 (1-" + (usablePokemon.size() + 1) + "): ");
            try {
                int choice = scanner.nextInt();
                if (choice == usablePokemon.size() + 1) {
                    return; // 取消
                }
                if (choice >= 1 && choice <= usablePokemon.size()) {
                    Pokemon selected = usablePokemon.get(choice - 1);
                    System.out.println("回来吧，" + player.getActivePokemon().getName() + "！");
                    player.setActivePokemon(selected);
                    System.out.println("去吧，" + selected.getName() + "！");
                    break;
                }
                System.out.println("无效的选择！");
            } catch (Exception e) {
                scanner.nextLine();
                System.out.println("请输入有效数字！");
            }
        }
    }

    private void opponentSwitchPokemonAI() {
        Pokemon current = opponent.getActivePokemon();
        List<Pokemon> usablePokemon = new ArrayList<>();

        for (Pokemon p : opponent.getParty()) {
            if (p != current && !p.isFainted()) {
                usablePokemon.add(p);
            }
        }

        if (usablePokemon.isEmpty()) {
            return;
        }

        // AI选择：优先选择克制对方宝可梦的宝可梦
        Pokemon playerPokemon = player.getActivePokemon();
        Pokemon bestSwitch = null;
        double bestAdvantage = 0;

        for (Pokemon p : usablePokemon) {
            double advantage = calculateTypeAdvantage(p, playerPokemon);
            if (advantage > bestAdvantage) {
                bestAdvantage = advantage;
                bestSwitch = p;
            }
        }

        if(bestSwitch==null){
            for (Pokemon p:usablePokemon) bestSwitch=p;
        }


        System.out.println(opponent.getName() + "收回了" + current.getName() + "！");
        opponent.setActivePokemon(bestSwitch);
        System.out.println(opponent.getName() + "派出了" + bestSwitch.getName() + "！");

    }

    private double calculateTypeAdvantage(Pokemon attacker, Pokemon defender) {
        double advantage = 0;
        for (Move move : attacker.getMoves()) {
            double moveEffectiveness = 1.0;
            for (Type defenderType : defender.getTypes()) {
                moveEffectiveness *= Type.getEffectiveness(move.getType(), defenderType);
            }
            if (moveEffectiveness > advantage) {
                advantage = moveEffectiveness;
            }
        }
        return advantage;
    }

    private void viewPokemonInfo() {
        System.out.println("\n=== 你的宝可梦信息 ===");
        for (Pokemon p : player.getParty()) {
            System.out.println(p.getName() + " Lv." + p.getLevel());
            System.out.println("HP: " + p.getCurrentHP() + "/" + p.getMaxHP());
            System.out.println("属性: " + getTypesString(p));
            System.out.println("技能:");
            for (Move move : p.getMoves()) {
                System.out.println("  " + move);
            }
            System.out.println();
        }
        System.out.println("======================\n");
    }

    private void attemptEscape() {
        System.out.println(player.getName() + "试图逃跑...");
        if (Math.random() < 0.9) { // 90%成功率
            System.out.println("成功逃跑了！");
            isBattleOver = true;
        } else {
            System.out.println("逃跑失败了！");
        }
    }

    private void checkBattleEnd() {
        if (!player.hasUsablePokemon()) {
            System.out.println("\n" + player.getName() + "的所有宝可梦都倒下了！");
            System.out.println(player.getName() + "输了！");
            isBattleOver = true;
        } else if (!opponent.hasUsablePokemon()) {
            System.out.println("\n" + opponent.getName() + "的所有宝可梦都倒下了！");
            System.out.println(player.getName() + "获胜！");
            isBattleOver = true;
        }
    }

    public boolean isBattleOver() {
        return isBattleOver;
    }
}