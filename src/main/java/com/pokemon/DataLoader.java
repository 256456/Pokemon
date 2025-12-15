package com.pokemon;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 提取为独立的顶级静态类以支持 TypeReference 正确解析
class MoveList {
    public List<DataLoader.MoveTemplate> moves;
}

class PokemonList {
    public List<DataLoader.PokemonTemplate> pokemons;
}

class PokemonMoves {
    public Map<String, List<String>> pokemon_moves;
}

public class DataLoader {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // 存储所有数据
    private static Map<String, PokemonTemplate> pokemonTemplates = new HashMap<>();
    private static Map<String, MoveTemplate> moveTemplates = new HashMap<>();
    private static Map<String, List<String>> pokemonMoves = new HashMap<>();

    public static class PokemonTemplate {
        public String name;
        public String[] types;
        public BaseStats baseStats;

        public static class BaseStats {
            public int hp;
            public int attack;
            public int defense;
            public int specialAttack;
            public int specialDefense;
            public int speed;
        }
    }

    public static class MoveTemplate {
        public String name;
        public String type;
        public String category;
        public int power;
        public int accuracy;
        public int pp;
        public String statusEffect; // 新增：状态效果
        public int statusChance;
    }

    // 加载所有数据
    public static void loadAllData() throws IOException {
        // 加载技能数据
        MoveList moveList = objectMapper.readValue(
                new File(DataLoader.class.getClassLoader().getResource("moves.json").getFile()),
                new TypeReference<MoveList>() {}
        );
        for (MoveTemplate move : moveList.moves) {
            moveTemplates.put(move.name, move);
        }

        System.out.println("已加载 " + moveTemplates.size() + " 个技能");

        // 加载宝可梦数据
        PokemonList pokemonList = objectMapper.readValue(
                new File(DataLoader.class.getClassLoader().getResource("pokemons.json").getFile()),
                new TypeReference<PokemonList>() {}
        );

        for (PokemonTemplate pokemon : pokemonList.pokemons) {
            pokemonTemplates.put(pokemon.name, pokemon);
        }

        System.out.println("已加载 " + pokemonTemplates.size() + " 只宝可梦");

        // 加载宝可梦可学技能关系
        PokemonMoves movesData = objectMapper.readValue(
                new File(DataLoader.class.getClassLoader().getResource("pokemon_moves.json").getFile()),
                new TypeReference<PokemonMoves>() {}
        );
        pokemonMoves = movesData.pokemon_moves;
        System.out.println("已加载宝可梦技能关系");
    }


    // 根据模板创建宝可梦
    public static Pokemon createPokemon(String name, int level) {
        PokemonTemplate template = pokemonTemplates.get(name);
        if (template == null) {
            throw new IllegalArgumentException("未知的宝可梦: " + name);
        }

        // 创建宝可梦
        Pokemon pokemon = new Pokemon(
                template.name,
                level,
                convertTypes(template.types),
                template.baseStats.hp,
                template.baseStats.attack,
                template.baseStats.defense,
                template.baseStats.specialAttack,
                template.baseStats.specialDefense,
                template.baseStats.speed
        );

        // 为宝可梦添加技能
        List<String> moveNames = pokemonMoves.get(name);
        if (moveNames != null) {
            for (String moveName : moveNames) {
                MoveTemplate moveTemplate = moveTemplates.get(moveName);
                if (moveTemplate != null) {
                    Move move = createMove(moveTemplate);
                    pokemon.learnMove(move);
                }
            }
        }

        return pokemon;
    }

    // 创建技能
    private static Move createMove(MoveTemplate template) {
        Type type = Type.valueOf(template.type);
        Move.Category category = Move.Category.valueOf(template.category);

        Status statusEffect = Status.NONE;
        if (template.statusEffect != null && !template.statusEffect.isEmpty()) {
            try {
                statusEffect = Status.valueOf(template.statusEffect.toUpperCase());
            } catch (IllegalArgumentException e) {
                statusEffect = Status.NONE;
            }
        }

        return new Move(
                template.name,
                type,
                category,
                template.power,
                template.accuracy,
                template.pp,
                statusEffect,
                template.statusChance
        );
    }

    // 转换类型字符串为Type枚举
    private static Type[] convertTypes(String[] typeStrings) {
        Type[] types = new Type[typeStrings.length];
        for (int i = 0; i < typeStrings.length; i++) {
            types[i] = Type.valueOf(typeStrings[i]);
        }
        return types;
    }

    // 获取所有宝可梦名称
    public static List<String> getAllPokemonNames() {
        return new ArrayList<>(pokemonTemplates.keySet());
    }

    // 获取宝可梦模板
    public static PokemonTemplate getPokemonTemplate(String name) {
        return pokemonTemplates.get(name);
    }
}
