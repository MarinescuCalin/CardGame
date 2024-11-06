package game;

import card.Card;
import card.CardFactory;
import card.HeroCard;
import fileio.CardInput;
import fileio.DecksInput;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Player {
    private final ArrayList<ArrayList<Card>> decks;

    @Getter
    private final ArrayList<Card> currentDeck;
    @Getter
    private Card heroCard;

    @Getter
    private ArrayList<Card> currentHand;
    @Getter
    private int mana;

    public Player() {
        currentDeck = new ArrayList<>();
        decks = new ArrayList<>();

        currentHand = new ArrayList<>();
        mana = 0;
    }

    public void addCardInHand() {
        if (currentDeck.isEmpty()) {
            return;
        }

        currentHand.add(currentDeck.remove(0));
    }

    public void chooseHero(final CardInput cardInput) {
        heroCard = new HeroCard(cardInput);
    }

    public void chooseStartingDeck(final int deckIdx, final int seed) {
        currentDeck.clear();

        currentDeck.addAll(decks.get(deckIdx));
        Collections.shuffle(currentDeck, new Random(seed));

        currentHand.clear();
        currentHand.add(currentDeck.remove(0));
    }

    public void decreaseMana(final int noMana) {
        mana -= noMana;
    }

    public void incrementMana(final int noMana) {
        mana += noMana;
    }

    public Card getHandCard(final int handIdx) {
        return currentHand.get(handIdx);
    }

    public void removeHandCard(final int handIdx) {
        currentHand.remove(handIdx);
    }

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
