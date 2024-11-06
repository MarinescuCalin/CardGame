package game;

import java.util.ArrayList;

public class Game {

    private Player player1;
    private Player player2;
    private int currentTurn;

    public Game() {
        player1 = new Player();
        player2 = new Player();
    }

    public ArrayList<Card> getPlayerDeck(int playerIdx) {
        return new ArrayList<>();
    }

    public void newGame() {
    }
}
