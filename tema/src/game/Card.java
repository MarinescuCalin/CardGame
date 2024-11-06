package game;

import fileio.CardInput;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public abstract class Card {
    private final int mana;
    private final int attack;
    private final int health;
    private final String description;
    private final ArrayList<Color> colors;
    private final String name;

    public Card(final CardInput cardInput) {
        mana = cardInput.getMana();
        attack = cardInput.getAttackDamage();
        health = cardInput.getHealth();
        description = cardInput.getDescription();
        name = cardInput.getName();

        colors = new ArrayList<>();
        for (String color : cardInput.getColors()) {
            colors.add(Color.fromString(color));
        }
    }
}
