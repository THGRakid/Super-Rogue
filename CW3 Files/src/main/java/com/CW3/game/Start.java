package com.CW3.game;

import com.CW3.game.Game;
import com.CW3.game.In;

import java.io.IOException;

public class Start {
    public static void main(String[] args) throws IOException {
        String FileA = "dungeonQ.txt";

        In stdin = new In(FileA);
        Game game = new Game(stdin);
        System.out.println(game);
        game.play();


    }
}
