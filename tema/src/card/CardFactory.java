package card;

import fileio.CardInput;

public class CardFactory {
    public static Card createCard(final CardInput card) {
        return switch (card.getName()) {
            case "Sentinel", "Berserker" -> new MinionCard(card);
            case "Goliath", "Warden" -> new TankCard(card);
            case "The Ripper", "Miraj", "The Cursed One", "Disciple" -> new AbilityCard(card);
            case "Lord Royce", "Empress Thorina", "King Mudface", "General Kocioraw" -> new HeroCard(card);
            default -> throw new IllegalStateException("Unexpected value: " + card.getName());
        };
    }
}
