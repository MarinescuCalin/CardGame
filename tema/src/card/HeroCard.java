package card;

import fileio.CardInput;

public class HeroCard extends Card {
    public HeroCard(CardInput cardInput) {
        super(cardInput);
        health = 30;
    }
}
