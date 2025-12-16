package com.pokemon;

import java.io.IOException;
import java.util.*;

public class GameManager {
    private Battle battle;
    private Scanner scanner;

    public GameManager() {
        this.scanner = new Scanner(System.in);
    }

    public void startGame() throws IOException {
        System.out.println("=== 宝可梦对战游戏 ===");

        while (true) {
            // 加载数据
            DataLoader.loadAllData();

            // 创建训练师
            Trainer player = new Trainer("你");
            Trainer opponent = new Trainer("小智");

            System.out.println("\n=== 组建队伍 ===");
            System.out.println("请选择你的宝可梦队伍（选择3只）：");

            // 选择玩家队伍
            selectPlayerTeam(player);

            // 设置对手队伍
            setupOpponentTeam(opponent);

            // 创建对战并开始
            startBattle(player, opponent);

            // 询问是否再玩一次
            System.out.print("\n再玩一次？(y/n): ");
            String choice = scanner.nextLine();
            if (!choice.equalsIgnoreCase("y")) {
                System.out.println("感谢游玩宝可梦对战游戏！");
                break;
            }

            System.out.println("\n=== 开始新游戏 ===\n");
        }

        scanner.close();
    }

    private void startBattle(Trainer player, Trainer opponent) {
        this.battle = new Battle(player, opponent);

        System.out.println("\n========== 对战开始！ ==========");
        System.out.println(player.getName() + " vs " + opponent.getName());
        System.out.println("================================\n");

        // 显示双方队伍
        System.out.println("=== 你的队伍 ===");
        for (Pokemon p : player.getParty()) {
            System.out.println(p.toString());
        }

        System.out.println("\n=== 对手的队伍 ===");
        for (Pokemon p : opponent.getParty()) {
            System.out.println(p.toString());
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
    }

    private void selectPlayerTeam(Trainer player) {
        List<String> allPokemonNames = DataLoader.getAllPokemonNames();

        System.out.println("\n=== 组建你的队伍 ===");
        System.out.println("请从以下宝可梦中选择3只组建队伍：");

        for (int i = 0; i < allPokemonNames.size(); i++) {
            String name = allPokemonNames.get(i);
            DataLoader.PokemonTemplate template = DataLoader.getPokemonTemplate(name);
            System.out.printf("%2d. %-8s (属性: %s/%s)%n",
                    i + 1,
                    template.name,
                    template.types[0],
                    template.types.length > 1 ? template.types[1] : ""
            );
        }

        Set<String> selectedNames = new HashSet<>();

        for (int i = 0; i < 3; i++) {
            System.out.print("\n请选择第" + (i + 1) + "只宝可梦 (1-" + allPokemonNames.size() + "): ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();

                if (choice < 1 || choice > allPokemonNames.size()) {
                    System.out.println("无效的选择，请重新选择！");
                    i--;
                    continue;
                }

                String selectedName = allPokemonNames.get(choice - 1);
                if (selectedNames.contains(selectedName)) {
                    System.out.println("不能重复选择相同的宝可梦！");
                    i--;
                    continue;
                }

                Pokemon pokemon = DataLoader.createPokemon(selectedName, 50);
                player.addPokemonToParty(pokemon);
                selectedNames.add(selectedName);

                System.out.println("已选择: " + selectedName);

            } catch (Exception e) {
                System.out.println("输入错误，请重新选择！");
                scanner.nextLine();
                i--;
            }
        }
    }

    private void setupOpponentTeam(Trainer opponent) {
        List<String> allPokemonNames = DataLoader.getAllPokemonNames();
        Collections.shuffle(allPokemonNames); // 随机打乱列表

        System.out.println("\n对手的队伍: ");
        for (int i = 0; i < Math.min(3, allPokemonNames.size()); i++) {
            String name = allPokemonNames.get(i);
            Pokemon pokemon = DataLoader.createPokemon(name, 50);
            opponent.addPokemonToParty(pokemon);
            System.out.println("- " + name);
        }
    }


}