package game;

import card.AbilityCard;
import card.Card;
import card.TankCard;
import fileio.Coordinates;
import fileio.DecksInput;
import fileio.StartGameInput;
import lombok.Getter;

import java.util.ArrayList;

public final class Game {
    private Player player1;
    private Player player2;
    private int currentTurn;
    private int noRound;
    private int startingPlayer;
    @Getter
    private int totalGamesPlayed;
    @Getter
    private int playerOneWins;
    @Getter
    private int playerTwoWins;

    private ArrayList<ArrayList<Card>> table;

    public Game() {
        player1 = new Player();
        player2 = new Player();

        table = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            table.add(new ArrayList<>());
        }

        totalGamesPlayed = 0;
        playerOneWins = 0;
        playerTwoWins = 0;
        noRound = 1;
    }

    public String cardUsesAbility(final Coordinates cardAttacker, final Coordinates cardAttacked) {
        int attackedX = cardAttacked.getX();
        int attackedY = cardAttacked.getY();

        Card attackerCard = table.get(cardAttacker.getX()).get(cardAttacker.getY());
        Card attackedCard = table.get(attackedX).get(attackedY);

        if (attackerCard.isAlreadyAttacked()) {
            return "Attacker card has already attacked this turn.";
        }

        if (attackedCard.isFrozen()) {
            return "Attacker card is frozen.";
        }

        String attackerCardName = attackerCard.getName();
        if (attackerCardName.equals("Disciple")) {
            if (currentTurn == 1 && (attackedX == 0 || attackedX == 1)) {
                return "Attacked card does not belong to the current player.";
            }

            if (currentTurn == 2 && (attackedX == 2 || attackedX == 3)) {
                return "Attacked card does not belong to the current player.";
            }

            attackedCard.setHealth(attackedCard.getHealth() + 2);
        } else {
            if (currentTurn == 1 && (attackedX == 2 || attackedX == 3)) {
                return "Attacked card does not belong to the enemy.";
            }

            if (currentTurn == 2 && (attackedX == 0 || attackedX == 1)) {
                return "Attacked card does not belong to the enemy.";
            }

            if (currentTurn == 1) {
                boolean foundTank = false;
                for (Card card : table.get(1)) {
                    if (card instanceof TankCard) {
                        foundTank = true;
                        break;
                    }
                }
                if (foundTank && !(attackedCard instanceof TankCard)) {
                    return "Attacked card is not of type 'Tank'.";
                }
            } else {
                boolean foundTank = false;
                for (Card card : table.get(2)) {
                    if (card instanceof TankCard) {
                        foundTank = true;
                        break;
                    }
                }

                if (foundTank && !(attackedCard instanceof TankCard)) {
                    return "Attacked card is not of type 'Tank'.";
                }
            }
        }

        switch (attackerCardName) {
            case "The Ripper" -> attackedCard.decreaseAttack(2);
            case "Miraj" -> {
                int health = attackerCard.getHealth();
                attackerCard.setHealth(attackedCard.getHealth());
                attackedCard.setHealth(health);
            }
            case "The Cursed One" -> {
                attackedCard.switchHealthAttack();
                if (attackedCard.getHealth() == 0) {
                    table.get(attackedX).remove(attackedCard);
                }
            }
            default -> {

            }
        }


        attackerCard.setAlreadyAttacked(true);
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

        if (attackedCard.isFrozen()) {
            return "Attacker card is frozen.";
        }

        if (currentTurn == 1) {
            boolean foundTank = false;
            for (Card card : table.get(1)) {
                if (card instanceof TankCard) {
                    foundTank = true;
                    break;
                }
            }
            if (foundTank && !(attackedCard instanceof TankCard)) {
                return "Attacked card is not of type 'Tank'.";
            }
        } else {
            boolean foundTank = false;
            for (Card card : table.get(2)) {
                if (card instanceof TankCard) {
                    foundTank = true;
                    break;
                }
            }

            if (foundTank && !(attackedCard instanceof TankCard)) {
                return "Attacked card is not of type 'Tank'.";
            }
        }

        if (attackedCard.getHealth() <= attackerCard.getAttack()) {
            table.get(attackedX).remove(attackedCard);
        } else {
            attackedCard.decreaseHealth(attackerCard.getAttack());
        }

        attackerCard.setAlreadyAttacked(true);

        return null;
    }

    /**
     * Gets the card at a specified position on the table.
     *
     * @param x The row index of the card.
     * @param y The column index of the card.
     * @return The card at the specified position, or null if not found.
     */
    public Card getCardAtPosition(final int x, final int y) {
        if (table.get(x).size() <= y) {
            return null;
        }

        return table.get(x).get(y);
    }

    /**
     * Returns the cards in the specified player's hand.
     *
     * @param playerIdx The index of the player (1 or 2).
     * @return An ArrayList of the cards in the player's hand.
     */
    public ArrayList<Card> getCardsInHand(final int playerIdx) {
        if (playerIdx == 1) {
            return player1.getCurrentHand();
        } else {
            return player2.getCurrentHand();
        }
    }

    /**
     * Returns all cards currently on the table.
     *
     * @return A 2D ArrayList of cards on the table.
     */
    public ArrayList<ArrayList<Card>> getCardsOnTable() {
        return table;
    }

    /**
     * Retrieves all frozen cards on the table.
     *
     * @return A list of frozen cards on the table.
     */
    public ArrayList<Card> getFrozenCardsOnTable() {
        ArrayList<Card> frozenCards = new ArrayList<>();

        for (ArrayList<Card> row : table) {
            for (Card card : row) {
                if (card.isFrozen()) {
                    frozenCards.add(card);
                }
            }
        }

        return frozenCards;
    }

    /**
     * Returns the current deck of the specified player.
     *
     * @param playerIdx The index of the player (1 or 2).
     * @return The player's current deck.
     */
    public ArrayList<Card> getPlayerDeck(final int playerIdx) {
        if (playerIdx == 1) {
            return player1.getCurrentDeck();
        } else {
            return player2.getCurrentDeck();
        }
    }

    /**
     * Retrieves the hero card of the specified player.
     *
     * @param playerIdx The index of the player (1 or 2).
     * @return The player's hero card.
     */
    public Card getPlayerHero(final int playerIdx) {
        if (playerIdx == 1) {
            return player1.getHeroCard();
        } else {
            return player2.getHeroCard();
        }
    }

    /**
     * Gets the current mana of the specified player.
     *
     * @param playerIdx The index of the player (1 or 2).
     * @return The player's current mana.
     */
    public int getPlayerMana(final int playerIdx) {
        if (playerIdx == 1) {
            return player1.getMana();
        } else {
            return player2.getMana();
        }
    }

    /**
     * Gets the current turn indicator (1 for player one, 2 for player two).
     *
     * @return The current player's turn.
     */
    public int getPlayerTurn() {
        return currentTurn;
    }


    /**
     * Ends the current player's turn and toggles the turn to the other player.
     * Resets attack status of cards and increments mana if a new round starts.
     */
    public void endPlayerTurn() {
        if (currentTurn == 1) {
            for (Card card : table.get(2)) {
                card.setFrozen(false);
            }
            for (Card card : table.get(3)) {
                card.setFrozen(false);
            }
            currentTurn = 2;
        } else {
            for (Card card : table.get(0)) {
                card.setFrozen(false);
            }
            for (Card card : table.get(1)) {
                card.setFrozen(false);
            }
            currentTurn = 1;
        }

        if (currentTurn == startingPlayer) {
            noRound++;
            int manaIncrement = noRound;
            if (manaIncrement > 10) {
                manaIncrement = 10;
            }

            player1.addCardInHand();
            player2.addCardInHand();

            player1.incrementMana(manaIncrement);
            player2.incrementMana(manaIncrement);

            player1.getHeroCard().setAlreadyAttacked(false);
            player2.getHeroCard().setAlreadyAttacked(false);

            for (ArrayList<Card> row : table) {
                for (Card card : row) {
                    card.setAlreadyAttacked(false);
                }
            }
        }
    }

    /**
     * Attempts to place a card from the player's hand onto the table.
     *
     * @param handIdx The index of the card in the player's hand.
     * @return A message if the action is invalid, or null if successful.
     */
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

    /**
     * Sets the decks for the specified player using the provided input.
     *
     * @param playerIdx  The index of the player (1 or 2).
     * @param decksInput The input data containing the decks.
     */
    public void setPlayerDecks(final int playerIdx, final DecksInput decksInput) {
        if (playerIdx == 1) {
            player1.setDecks(decksInput);
        } else {
            player2.setDecks(decksInput);
        }
    }

    /**
     * Starts the game using the specified starting configurations.
     *
     * @param startGameInput The initial game configuration.
     */
    public void startGame(final StartGameInput startGameInput) {
        for (ArrayList<Card> row : table) {
            row.clear();
        }

        currentTurn = startGameInput.getStartingPlayer();
        startingPlayer = currentTurn;

        player1.chooseStartingDeck(startGameInput.getPlayerOneDeckIdx(),
                startGameInput.getShuffleSeed());
        player1.chooseHero(startGameInput.getPlayerOneHero());

        player2.chooseStartingDeck(startGameInput.getPlayerTwoDeckIdx(),
                startGameInput.getShuffleSeed());
        player2.chooseHero(startGameInput.getPlayerTwoHero());

        noRound = 1;

        player1.setMana(1);
        player2.setMana(1);

        totalGamesPlayed++;
    }

    /**
     * Allows a card to attack the opposing player's hero.
     *
     * @param cardAttacker The coordinates of the attacking card.
     * @return A message if the attack was invalid, or null if the hero was attacked successfully.
     */
    public String useAttackHero(final Coordinates cardAttacker) {
        Card attackerCard = table.get(cardAttacker.getX()).get(cardAttacker.getY());

        if (attackerCard.isFrozen()) {
            return "Attacker card is frozen.";
        }

        if (attackerCard.isAlreadyAttacked()) {
            return "Attacker card has already attacked this turn.";
        }

        if (currentTurn == 1) {
            boolean foundTank = false;
            for (Card card : table.get(1)) {
                if (card instanceof TankCard) {
                    foundTank = true;
                    break;
                }
            }
            if (foundTank) {
                return "Attacked card is not of type 'Tank'.";
            }
        } else {
            boolean foundTank = false;
            for (Card card : table.get(2)) {
                if (card instanceof TankCard) {
                    foundTank = true;
                    break;
                }
            }

            if (foundTank) {
                return "Attacked card is not of type 'Tank'.";
            }
        }

        if (currentTurn == 1) {
            player2.getHeroCard().decreaseHealth(attackerCard.getAttack());
            if (player2.getHeroCard().getHealth() <= 0) {
                playerOneWins++;
                return "Player one killed the enemy hero.";
            }
        } else {
            player1.getHeroCard().decreaseHealth(attackerCard.getAttack());
            if (player1.getHeroCard().getHealth() <= 0) {
                playerTwoWins++;
                return "Player two killed the enemy hero.";
            }
        }

        attackerCard.setAlreadyAttacked(true);

        return null;
    }

    /**
     * Uses the current player's hero ability on a specified row.
     *
     * @param affectedRow The index of the row affected by the hero's ability.
     * @return A message if the action is invalid, or null if successful.
     */
    public String useHeroAbility(final int affectedRow) {
        Card heroCard;

        if (currentTurn == 1) {
            heroCard = player1.getHeroCard();
            if (player1.getMana() < player1.getHeroCard().getMana()) {
                return "Not enough mana to use hero's ability.";
            }
        } else {
            heroCard = player2.getHeroCard();
            if (player2.getMana() < player2.getHeroCard().getMana()) {
                return "Not enough mana to use hero's ability.";
            }
        }

        if (heroCard.isAlreadyAttacked()) {
            return "Hero has already attacked this turn.";
        }

        if (heroCard.getName().equals("Lord Royce")
                || heroCard.getName().equals("Empress Thorina")) {
            if (currentTurn == 1 && (affectedRow == 2 || affectedRow == 3)) {
                return "Selected row does not belong to the enemy.";
            } else if (currentTurn == 2 && (affectedRow == 0 || affectedRow == 1)) {
                return "Selected row does not belong to the enemy.";
            }

            if (heroCard.getName().equals("Lord Royce")) {
                for (Card card : table.get(affectedRow)) {
                    card.setFrozen(true);
                }
            } else {
                Card maxHealthCard = null;
                int maxHealth = 0;
                for (Card card : table.get(affectedRow)) {
                    if (card.getHealth() > maxHealth) {
                        maxHealth = card.getHealth();
                        maxHealthCard = card;
                    }
                }

                if (maxHealthCard != null) {
                    table.get(affectedRow).remove(maxHealthCard);
                }
            }

        } else {
            if (currentTurn == 2 && (affectedRow == 2 || affectedRow == 3)) {
                return "Selected row does not belong to the current player.";
            } else if (currentTurn == 1 && (affectedRow == 0 || affectedRow == 1)) {
                return "Selected row does not belong to the current player.";
            }

            if (heroCard.getName().equals("King Mudface")) {
                for (Card card : table.get(affectedRow)) {
                    card.setHealth(card.getHealth() + 1);
                }
            } else {
                for (Card card : table.get(affectedRow)) {
                    card.setAttack(card.getAttack() + 1);
                }
            }
        }

        if (currentTurn == 1) {
            player1.decreaseMana(heroCard.getMana());
        } else {
            player2.decreaseMana(heroCard.getMana());
        }

        heroCard.setAlreadyAttacked(true);

        return null;
    }
}
