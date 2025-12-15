package com.pokemon;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("=== 宝可梦对战游戏 ===");

        // 创建游戏管理器并启动游戏
        GameManager gameManager = new GameManager();
        gameManager.startGame();
    }
}