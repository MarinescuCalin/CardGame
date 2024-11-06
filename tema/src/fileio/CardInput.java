package fileio;

import java.util.ArrayList;
import java.util.Locale;

import card.Card;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import game.Color;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class CardInput {
    private int mana;
    private int attackDamage;
    private int health;
    private String description;
    private ArrayList<String> colors;
    private String name;

    public CardInput() {
    }

    public CardInput(final Card card) {
        mana = card.getMana();
        attackDamage = card.getAttack();
        health = card.getHealth();
        description = card.getDescription();
        name = card.getName();

        colors = new ArrayList<>();
        for (Color color : card.getColors()) {
            colors.add(color.name());
        }
    }

    public int getMana() {
        return mana;
    }

    public void setMana(final int mana) {
        this.mana = mana;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(final int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(final int health) {
        this.health = health;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public ArrayList<String> getColors() {
        return colors;
    }

    public void setColors(final ArrayList<String> colors) {
        this.colors = colors;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public ObjectNode toObjectNode(ObjectMapper objectMapper) {
        ObjectNode result = objectMapper.createObjectNode();

        result.put("mana", mana);
        result.put("attackDamage", attackDamage);
        result.put("health", health);
        result.put("description", description);
        result.put("name", name);

        ArrayNode colorsArray = objectMapper.createArrayNode();
        for (String color : colors) {
            String lowerCase = color.toLowerCase();
            colorsArray.add(lowerCase.substring(0, 1).toUpperCase() + lowerCase.substring(1));
        }
        result.set("colors", colorsArray);

        return result;
    }

    @Override
    public String toString() {
        return "CardInput{"
                + "mana="
                + mana
                + ", attackDamage="
                + attackDamage
                + ", health="
                + health
                + ", description='"
                + description
                + '\''
                + ", colors="
                + colors
                + ", name='"
                + ""
                + name
                + '\''
                + '}';
    }
}
