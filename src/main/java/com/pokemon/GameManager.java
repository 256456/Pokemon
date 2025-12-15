package com.pokemon;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameManager {
    private Battle battle;
    private Scanner scanner;

    public GameManager(Battle battle) {
        this.battle = battle;
        this.scanner = new Scanner(System.in);
    }

    public void startBattle() {
        System.out.println("\n========== 对战开始！ ==========");
        System.out.println(battle.player.getName() + " vs " + battle.opponent.getName());
        System.out.println("================================\n");

        // 显示双方队伍
        System.out.println("=== 你的队伍 ===");
        for (Pokemon p : battle.player.getParty()) {
            System.out.println(p.getName() + " Lv." + p.getLevel() + " (属性: " +
                    getTypesString(p.getTypes()) + ")");
        }

        System.out.println("\n=== 对手的队伍 ===");
        for (Pokemon p : battle.opponent.getParty()) {
            System.out.println(p.getName() + " Lv." + p.getLevel() + " (属性: " +
                    getTypesString(p.getTypes()) + ")");
        }

        System.out.println("\n按回车键开始对战...");
        scanner.nextLine();

        // 对战循环
        while (!battle.isBattleOver()) {
            battle.executeTurn();

            if (!battle.isBattleOver()) {
                System.out.println("\n按回车键继续下一回合...");
                scanner.nextLine();
            }
        }

        System.out.println("\n========== 对战结束！ ==========");

        // 询问是否再玩一次
        System.out.print("\n再玩一次？(y/n): ");
        String choice = scanner.nextLine();
        if (choice.equalsIgnoreCase("y")) {
            // 重置对战
            resetGame();
        } else {
            System.out.println("感谢游玩宝可梦对战游戏！");
        }
    }

    private String getTypesString(Type[] types) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < types.length; i++) {
            sb.append(types[i]);
            if (i < types.length - 1) {
                sb.append("/");
            }
        }
        return sb.toString();
    }

    private void resetGame() {
        System.out.println("\n=== 重新开始游戏 ===");

        // 重新创建训练师
        Trainer player = new Trainer("小明");
        Trainer opponent = new Trainer("小智");

        // 重新创建宝可梦
        Pokemon charizard = new Pokemon("喷火龙", 50,
                new Type[]{Type.FIRE, Type.FLYING},
                78, 84, 78, 109, 85, 100);

        Pokemon blastoise = new Pokemon("水箭龟", 50,
                new Type[]{Type.WATER},
                79, 83, 100, 85, 105, 78);

        Pokemon venusaur = new Pokemon("妙蛙花", 50,
                new Type[]{Type.GRASS, Type.POISON},
                80, 82, 83, 100, 100, 80);

        Pokemon pikachu = new Pokemon("皮卡丘", 50,
                new Type[]{Type.ELECTRIC},
                35, 55, 40, 50, 50, 90);

        Pokemon gyarados = new Pokemon("暴鲤龙", 50,
                new Type[]{Type.WATER, Type.FLYING},
                95, 125, 79, 60, 100, 81);

        Pokemon dragonite = new Pokemon("快龙", 50,
                new Type[]{Type.DRAGON, Type.FLYING},
                91, 134, 95, 100, 100, 80);

        Pokemon snorlax = new Pokemon("卡比兽", 50,
                new Type[]{Type.NORMAL},
                160, 110, 65, 65, 110, 30);

        Pokemon alakazam = new Pokemon("胡地", 50,
                new Type[]{Type.PSYCHIC},
                55, 50, 45, 135, 85, 120);

        // 添加技能
        Move flamethrower = new Move("喷射火焰", Type.FIRE, Move.Category.SPECIAL, 90, 100, 15);
        Move hydroPump = new Move("水炮", Type.WATER, Move.Category.SPECIAL, 110, 80, 5);
        Move solarBeam = new Move("阳光烈焰", Type.GRASS, Move.Category.SPECIAL, 120, 100, 10);
        Move thunderbolt = new Move("十万伏特", Type.ELECTRIC, Move.Category.SPECIAL, 90, 100, 15);
        Move earthquake = new Move("地震", Type.GROUND, Move.Category.PHYSICAL, 100, 100, 10);
        Move iceBeam = new Move("急冻光线", Type.ICE, Move.Category.SPECIAL, 90, 100, 10);
        Move dragonClaw = new Move("龙爪", Type.DRAGON, Move.Category.PHYSICAL, 80, 100, 15);
        Move aerialAce = new Move("燕返", Type.FLYING, Move.Category.PHYSICAL, 60, 100, 20);
        Move hyperBeam = new Move("破坏光线", Type.NORMAL, Move.Category.SPECIAL, 150, 90, 5);
        Move bodySlam = new Move("泰山压顶", Type.NORMAL, Move.Category.PHYSICAL, 85, 100, 15);
        Move psychic = new Move("幻象术", Type.PSYCHIC, Move.Category.SPECIAL, 90, 100, 10);
        Move thunderPunch = new Move("雷电拳", Type.ELECTRIC, Move.Category.PHYSICAL, 75, 100, 15);
        Move fireBlast = new Move("大字爆炎", Type.FIRE, Move.Category.SPECIAL, 110, 85, 5);

        // 给宝可梦分配技能
        charizard.learnMove(flamethrower);
        charizard.learnMove(dragonClaw);
        charizard.learnMove(aerialAce);
        charizard.learnMove(earthquake);

        blastoise.learnMove(hydroPump);
        blastoise.learnMove(iceBeam);
        blastoise.learnMove(earthquake);
        blastoise.learnMove(bodySlam);

        venusaur.learnMove(solarBeam);
        venusaur.learnMove(earthquake);
        venusaur.learnMove(bodySlam);

        pikachu.learnMove(thunderbolt);
        pikachu.learnMove(iceBeam);
        pikachu.learnMove(bodySlam);

        gyarados.learnMove(hydroPump);
        gyarados.learnMove(iceBeam);
        gyarados.learnMove(earthquake);
        gyarados.learnMove(bodySlam);

        dragonite.learnMove(dragonClaw);
        dragonite.learnMove(iceBeam);
        dragonite.learnMove(thunderbolt);
        dragonite.learnMove(hyperBeam);

        snorlax.learnMove(bodySlam);
        snorlax.learnMove(earthquake);
        snorlax.learnMove(iceBeam);
        snorlax.learnMove(hyperBeam);

        alakazam.learnMove(psychic);
        alakazam.learnMove(thunderPunch);
        alakazam.learnMove(iceBeam);
        alakazam.learnMove(fireBlast);

        // 玩家选择队伍
        List<Pokemon> allPokemon = new ArrayList<>();
        allPokemon.add(charizard);
        allPokemon.add(blastoise);
        allPokemon.add(venusaur);
        allPokemon.add(pikachu);
        allPokemon.add(gyarados);
        allPokemon.add(dragonite);
        allPokemon.add(snorlax);
        allPokemon.add(alakazam);

        List<Pokemon> playerTeam = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            System.out.println("\n可选择的宝可梦：");
            for (int j = 0; j < allPokemon.size(); j++) {
                Pokemon p = allPokemon.get(j);
                System.out.println((j+1) + ". " + p.getName() + " (属性: " +
                        getTypesString(p.getTypes()) + ")");
            }

            System.out.print("请选择第" + (i+1) + "只宝可梦 (1-" + allPokemon.size() + "): ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice >= 1 && choice <= allPokemon.size()) {
                Pokemon selected = allPokemon.get(choice - 1);
                playerTeam.add(selected);
                allPokemon.remove(choice - 1);
                System.out.println("已选择: " + selected.getName());
            } else {
                System.out.println("无效的选择，请重新选择！");
                i--;
            }
        }

        // 将选择的宝可梦加入玩家队伍
        for (Pokemon p : playerTeam) {
            player.addPokemonToParty(p);
        }

        // 对手队伍
        opponent.addPokemonToParty(blastoise);
        opponent.addPokemonToParty(venusaur);
        opponent.addPokemonToParty(dragonite);

        // 开始新的对战
        this.battle = new Battle(player, opponent);
        startBattle();
    }
}