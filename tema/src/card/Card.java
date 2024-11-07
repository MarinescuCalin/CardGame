package card;

import fileio.CardInput;
import game.Color;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
public abstract class Card {
    protected final int mana;

    @Setter
    protected int attack;

    @Setter
    protected int health;
    protected final String description;
    protected final ArrayList<Color> colors;
    protected final String name;

    @Setter
    protected boolean frozen;

    @Setter
    protected boolean alreadyAttacked;

    public Card(final CardInput cardInput) {
        mana = cardInput.getMana();
        attack = cardInput.getAttackDamage();
        health = cardInput.getHealth();
        description = cardInput.getDescription();
        name = cardInput.getName();

        alreadyAttacked = false;
        frozen = false;

        colors = new ArrayList<>();
        for (String color : cardInput.getColors()) {
            colors.add(Color.fromString(color));
        }
    }

    public Card(final Card card) {
        mana = card.mana;
        attack = card.attack;
        health = card.health;
        description = card.description;
        name = card.name;

        alreadyAttacked = false;
        frozen = false;

        colors = new ArrayList<>();
        colors.addAll(card.getColors());
    }

    public void decreaseAttack(final int noAttack) {
        attack -= noAttack;
        if (attack < 0) {
            attack = 0;
        }
    }

    public void decreaseHealth(final int noHealth) {
        health -= noHealth;
    }

    public void switchHealthAttack() {
        int aux = health;
        health = attack;
        attack = aux;
    }
}
