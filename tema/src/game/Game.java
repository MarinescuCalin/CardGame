package game;

import card.AbilityCard;
import card.Card;
import card.TankCard;
import fileio.Coordinates;
import fileio.DecksInput;
import fileio.StartGameInput;

import java.util.ArrayList;

public class Game {

    private final Player player1;
    private final Player player2;
    private int currentTurn;
    private int noRound;
    private int startingPlayer;

    private ArrayList<ArrayList<Card>> table;

    public Game() {
        player1 = new Player();
        player2 = new Player();

        table = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            table.add(new ArrayList<>());

        noRound = 1;
    }

    public String cardUsesAbility(final Coordinates cardAttacker, final Coordinates cardAttacked) {
        int attackedX = cardAttacked.getX();
        int attackedY = cardAttacked.getY();

        Card attackerCard = table.get(cardAttacker.getX()).get(cardAttacker.getY());
        Card attackedCard = table.get(attackedX).get(attackedY);

        if (attackerCard.getName().equals("The Ripper")) {
            attackedCard.decreaseAttack(2);
        } else if (attackerCard.getName().equals("Miraj")) {
            int health = attackerCard.getHealth();
            attackerCard.setHealth(attackedCard.getHealth());
            attackedCard.setHealth(health);
        } else if (attackerCard.getName().equals("The Cursed One")) {
            attackedCard.switchHealthAttack();
            if (attackedCard.getHealth() == 0) {
                table.get(attackedX).remove(attackedCard);
            }
        } else if (attackerCard.getName().equals("Disciple")) {
            attackedCard.setHealth(attackedCard.getHealth() + 2);
        }

        return null;
    }


    public String cardUsesAttack(final Coordinates cardAttacker, final Coordinates cardAttacked) {
        int attackedX = cardAttacked.getX();
        int attackedY = cardAttacked.getY();

        Card attackerCard = table.get(cardAttacker.getX()).get(cardAttacker.getY());
        Card attackedCard = table.get(attackedX).get(attackedY);

        if (currentTurn == 1 && (attackedX == 2 || attackedX == 3)) {
            return "Attacked card does not belong to the enemy.";
        }

        if (currentTurn == 2 && (attackedX == 0 || attackedX == 1)) {
            return "Attacked card does not belong to the enemy.";
        }

        if (attackerCard.isAlreadyAttacked()) {
            return "Attacker card has already attacked this turn.";
        }

        if (attackedX == 3 && table.get(2).
                size() > attackedY) {
            return "Attacked card is not of type 'Tank'.";
        } else if (attackedX == 0 && table.get(1).size() > attackedY) {
            return "Attacked card is not of type 'Tank'.";
        }

        if (attackedCard.getHealth() <= attackerCard.getAttack()) {
            table.get(attackedX).remove(attackedCard);
        } else {
            attackedCard.decreaseHealth(attackerCard.getAttack());
        }

        attackerCard.setAlreadyAttacked(true);
        return null;
    }

    public Card getCardAtPosition(final int x, final int y) {
        if (table.get(x).size() <= y) {
            return null;
        }

        return table.get(x).get(y);
    }

    public ArrayList<Card> getCardsInHand(final int playerIdx) {
        if (playerIdx == 1) {
            return player1.getCurrentHand();
        } else {
            return player2.getCurrentHand();
        }
    }

    public ArrayList<ArrayList<Card>> getCardsOnTable() {
        return table;
    }

    public ArrayList<Card> getPlayerDeck(final int playerIdx) {
        if (playerIdx == 1) {
            return player1.getCurrentDeck();
        } else {
            return player2.getCurrentDeck();
        }
    }

    public Card getPlayerHero(final int playerIdx) {
        if (playerIdx == 1) {
            return player1.getHeroCard();
        } else {
            return player2.getHeroCard();
        }
    }

    public int getPlayerMana(final int playerIdx) {
        if (playerIdx == 1) {
            return player1.getMana();
        } else {
            return player2.getMana();
        }
    }

    public int getPlayerTurn() {
        return currentTurn;
    }

    public void endPlayerTurn() {
        if (currentTurn == 1) {
            currentTurn = 2;
        } else {
            currentTurn = 1;
        }

        if (currentTurn == startingPlayer) {
            noRound++;
            int manaIncrement = noRound;
            if (manaIncrement > 10)
                manaIncrement = 10;

            player1.addCardInHand();
            player2.addCardInHand();

            player1.incrementMana(manaIncrement);
            player2.incrementMana(manaIncrement);

            for (ArrayList<Card> row : table) {
                for (Card card : row) {
                    card.setAlreadyAttacked(false);
                }
            }
        }
    }

    public String placeCard(final int handIdx) {
        if (currentTurn == 1) {
            Card card = player1.getHandCard(handIdx);

            if (card.getMana() > player1.getMana()) {
                return "Not enough mana to place card on table.";
            }

            player1.removeHandCard(handIdx);
            player1.decreaseMana(card.getMana());

            int row = 3;

            if (card instanceof TankCard) {
                row = 2;
            } else if (card instanceof AbilityCard) {
                if (card.getName().equals("The Ripper") || card.getName().equals("Miraj")) {
                    row = 2;
                }
            }
            table.get(row).add(card);
        } else {
            Card card = player2.getHandCard(handIdx);

            if (card.getMana() > player2.getMana()) {
                return "Not enough mana to place card on table.";
            }

            player2.removeHandCard(handIdx);
            player2.decreaseMana(card.getMana());

            int row = 0;
            if (card instanceof TankCard) {
                row = 1;
            } else if (card instanceof AbilityCard) {
                if (card.getName().equals("The Ripper") || card.getName().equals("Miraj")) {
                    row = 1;
                }
            }
            table.get(row).add(card);
        }

        return null;
    }

    public void setPlayerDecks(final int playerIdx, final DecksInput decksInput) {
        if (playerIdx == 1) {
            player1.setDecks(decksInput);
        } else {
            player2.setDecks(decksInput);
        }
    }

    public void startGame(final StartGameInput startGameInput) {
        for (ArrayList<Card> row : table) {
            row.clear();
        }

        currentTurn = startGameInput.getStartingPlayer();
        startingPlayer = currentTurn;

        player1.chooseStartingDeck(startGameInput.getPlayerOneDeckIdx(), startGameInput.getShuffleSeed());
        player1.chooseHero(startGameInput.getPlayerOneHero());

        player2.chooseStartingDeck(startGameInput.getPlayerTwoDeckIdx(), startGameInput.getShuffleSeed());
        player2.chooseHero(startGameInput.getPlayerTwoHero());

        noRound = 1;
        player1.incrementMana(noRound);
        player2.incrementMana(noRound);
    }
}
