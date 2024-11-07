package card;

import fileio.CardInput;

public class HeroCard extends Card {
    public HeroCard(final CardInput cardInput) {
        super(cardInput);
        health = 30;
    }
}
