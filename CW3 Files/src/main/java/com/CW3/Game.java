package com.CW3;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 游戏开始
 */
public class Game {

    // portable newline
    private final static String NEWLINE = System.getProperty("line.separator");

    private Dungeon dungeon;     // the dungeon
    private char MONSTER;        // name of the monster (A - Z)
    private char ROGUE = '@';    // name of the rogue
    private int N;               // board dimension
    private Site monsterSite;    // location of monster
    private Site rogueSite;      // location of rogue
    private Monster monster;     // the monster
    private Rogue rogue;         // the rogue
    private Boolean status = true;      // status of programme

    public Game() {
    }

    public Game(Boolean status){
        this.status = status;
    }
    // initialize board from file
    public Game(In in) {
        // read in data
        N = Integer.parseInt(in.readLine());
        char[][] board = new char[N][N];
        for (int i = 0; i < N; i++) {
            String s = in.readLine();
            for (int j = 0; j < N; j++) {
                board[i][j] = s.charAt(2 * j);

                // check for monster's location
                if (board[i][j] >= 'A' && board[i][j] <= 'Z') {
                    MONSTER = board[i][j];
                    board[i][j] = '.';
                    monsterSite = new Site(i, j);
                }

                // check for rogue's location
                if (board[i][j] == ROGUE) {
                    board[i][j] = '.';
                    rogueSite = new Site(i, j);
                }
            }
        }
        dungeon = new Dungeon(board);
        monster = new Monster(this);
        rogue = new Rogue(this);
    }

    // return position of monster and rogue
    public Site getMonsterSite() {
        return monsterSite;
    }

    public Site getRogueSite() {
        return rogueSite;
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    public Boolean getStatus() { return status; }

    // play until monster catches the rogue
    public void play() {
        for (int t = 1; true; t++) {
            System.out.println("Move " + t);
            System.out.println();

            // monster moves
            if (monsterSite.equals(rogueSite)) break;
            Site next = monster.move();
            if (dungeon.isLegalMove(monsterSite, next)) monsterSite = next;
            else throw new RuntimeException("Monster caught cheating");
            System.out.println(this);

            // rogue moves
            if (monsterSite.equals(rogueSite)) break;
            next = rogue.move();
            if (dungeon.isLegalMove(rogueSite, next)) rogueSite = next;
            else throw new RuntimeException("Rogue caught cheating");
            System.out.println(this);
        }

        System.out.println("\n------------------------");
        System.out.println("|   CONGRATULATIONS!    |");
        System.out.println("|   Monster has won!    |");
        System.out.println("|   Score: Over 100     |");
        System.out.println("------------------------\n");

    }

    public List<Game> playForAWT() {
        List<Game> displayGame = new ArrayList<>();

        // monster moves
        if (monsterSite.equals(rogueSite)) {
            displayGame.add(new Game(false));
            return displayGame;
        }
        Site next = monster.move();
        if (dungeon.isLegalMove(monsterSite, next)) monsterSite = next;
        else throw new RuntimeException("Monster caught cheating");
        displayGame.add(this);

        // rogue moves
        if (monsterSite.equals(rogueSite)) {

            displayGame.add(new Game(false));
            return displayGame;
        }
        next = rogue.move();
        if (dungeon.isLegalMove(rogueSite, next)) rogueSite = next;
        else throw new RuntimeException("Rogue caught cheating");
        displayGame.add(this);

        return displayGame;
    }

    // string representation of game state (inefficient because of Site and string concat)
    public String toString() {
        String s = "";
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                Site site = new Site(i, j);
                if (rogueSite.equals(monsterSite) && (rogueSite.equals(site))) s += "* ";
                else if (rogueSite.equals(site)) s += ROGUE + " ";
                else if (monsterSite.equals(site)) s += MONSTER + " ";
                else if (dungeon.isRoom(site)) s += ". ";
                else if (dungeon.isCorridor(site)) s += "+ ";
                else if (dungeon.isRoom(site)) s += ". ";
                else if (dungeon.isWall(site)) s += "  ";
            }
            s += NEWLINE;
        }
        return s;
    }

}
