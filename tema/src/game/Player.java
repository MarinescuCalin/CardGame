package game;

import card.Card;
import card.CardFactory;
import card.HeroCard;
import fileio.CardInput;
import fileio.DecksInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * The Player class represents a player in the game with a hero card,
 * a deck of cards, a current hand of cards, and a mana count.
 * This class includes methods to manage the player's deck, hand, and mana.
 */
public final class Player {

    /**
     * A collection of decks that the player can choose from.
     */
    private final ArrayList<ArrayList<Card>> decks;

    /**
     * The currently selected deck that the player is playing with.
     */
    @Getter
    private final ArrayList<Card> currentDeck;

    /**
     * The hero card that the player has chosen for the game.
     */
    @Getter
    private Card heroCard;

    /**
     * The player's current hand of cards, drawn from the current deck.
     */
    @Getter
    private ArrayList<Card> currentHand;

    /**
     * The player's current mana level.
     */
    @Getter
    @Setter
    private int mana;

    /**
     * Constructs a new Player with an empty deck, empty hand, and 0 mana.
     */
    public Player() {
        currentDeck = new ArrayList<>();
        decks = new ArrayList<>();
        currentHand = new ArrayList<>();
        mana = 0;
    }

    /**
     * Adds the top card from the current deck to the player's hand.
     * If the deck is empty, no card is added.
     */
    public void addCardInHand() {
        if (currentDeck.isEmpty()) {
            return;
        }
        currentHand.add(currentDeck.remove(0));
    }

    /**
     * Selects a hero card for the player using the specified card input.
     *
     * @param cardInput The input data used to create the hero card.
     */
    public void chooseHero(final CardInput cardInput) {
        heroCard = new HeroCard(cardInput);
    }

    /**
     * Chooses the starting deck for the player by specifying the index of the deck.
     * The deck is shuffled using the specified seed, and the first card is added to the hand.
     *
     * @param deckIdx The index of the deck to be selected from available decks.
     * @param seed    The seed value used to shuffle the selected deck.
     */
    public void chooseStartingDeck(final int deckIdx, final int seed) {
        currentDeck.clear();
        for (Card card : decks.get(deckIdx)) {
            currentDeck.add(CardFactory.createCard(new CardInput(card)));
        }
        Collections.shuffle(currentDeck, new Random(seed));
        currentHand.clear();
        currentHand.add(currentDeck.remove(0));
    }

    /**
     * Decreases the player's mana by a specified amount.
     *
     * @param noMana The amount of mana to subtract from the player's current mana.
     */
    public void decreaseMana(final int noMana) {
        mana -= noMana;
    }

    /**
     * Increases the player's mana by a specified amount.
     *
     * @param noMana The amount of mana to add to the player's current mana.
     */
    public void incrementMana(final int noMana) {
        mana += noMana;
    }

    /**
     * Retrieves a card from the player's hand by index.
     *
     * @param handIdx The index of the card in the player's hand to retrieve.
     * @return The card at the specified index in the player's hand.
     */
    public Card getHandCard(final int handIdx) {
        return currentHand.get(handIdx);
    }

    /**
     * Removes a card from the player's hand by index.
     *
     * @param handIdx The index of the card to be removed from the player's hand.
     */
    public void removeHandCard(final int handIdx) {
        currentHand.remove(handIdx);
    }

    /**
     * Sets the available decks for the player from the provided input data.
     *
     * @param decksInput The input data containing decks with card information.
     */
    public void setDecks(final DecksInput decksInput) {
        for (ArrayList<CardInput> deckInput : decksInput.getDecks()) {
            ArrayList<Card> deck = new ArrayList<>();
            for (CardInput cardInput : deckInput) {
                deck.add(CardFactory.createCard(cardInput));
            }
            decks.add(deck);
        }
    }
}
