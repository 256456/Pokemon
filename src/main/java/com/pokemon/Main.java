package com.pokemon;

import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("=== 宝可梦对战游戏 ===");
        DataLoader.loadAllData();
        // 创建训练师
        Trainer player = new Trainer("你");
        Trainer opponent = new Trainer("小智");


        System.out.println("\n=== 组建队伍 ===");
        System.out.println("请选择你的宝可梦队伍（选择3只）：");

        selectPlayerTeam(player);
        setupOpponentTeam(opponent);

        // 开始对战
        Battle battle = new Battle(player, opponent);
        GameManager gameManager = new GameManager(battle);
        gameManager.startBattle();
    }

    private static void selectPlayerTeam(Trainer player) {
        Scanner scanner = new Scanner(System.in);
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

                // 创建宝可梦并加入队伍
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

    private static void setupOpponentTeam(Trainer opponent) {
        List<String> allPokemonNames = DataLoader.getAllPokemonNames();
        Collections.shuffle(allPokemonNames); // 随机打乱列表

        System.out.println("\n对手的队伍: ");
        // 选择前3只作为对手队伍
        for (int i = 0; i < Math.min(3, allPokemonNames.size()); i++) {
            String name = allPokemonNames.get(i);
            Pokemon pokemon = DataLoader.createPokemon(name, 50);
            opponent.addPokemonToParty(pokemon);
            System.out.println("- " + name);
        }
    }


}

