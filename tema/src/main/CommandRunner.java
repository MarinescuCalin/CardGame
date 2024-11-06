package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.CardInput;
import card.Card;
import fileio.Coordinates;
import game.Game;

import java.util.ArrayList;

public class CommandRunner {
    private Game game;
    private ObjectMapper objectMapper;

    public CommandRunner(final ObjectMapper objectMapper, final Game game) {
        this.objectMapper = objectMapper;
        this.game = game;
    }

    public ObjectNode executeAction(ActionsInput actionsInput) {
        return switch (actionsInput.getCommand()) {
            case "cardUsesAbility" -> cardUsesAbility(actionsInput.getCardAttacker(), actionsInput.getCardAttacked());
            case "cardUsesAttack" -> cardUsesAttack(actionsInput.getCardAttacker(), actionsInput.getCardAttacked());
            case "endPlayerTurn" -> endPlayerTurn();
            case "getCardAtPosition" -> getCardAtPosition(actionsInput.getX(), actionsInput.getY());
            case "getCardsInHand" -> getCardsInHand(actionsInput.getPlayerIdx());
            case "getCardsOnTable" -> getCardsOnTable();
            case "getFrozenCardsOnTable" -> getFrozenCardsOnTable();
            case "getPlayerDeck" -> getPlayerDeck(actionsInput.getPlayerIdx());
            case "getPlayerHero" -> getPlayerHero(actionsInput.getPlayerIdx());
            case "getPlayerMana" -> getPlayerMana(actionsInput.getPlayerIdx());
            case "getPlayerOneWins" -> getPlayerOneWins();
            case "getPlayerTwoWins" -> getPlayerTwoWins();
            case "getPlayerTurn" -> getPlayerTurn();
            case "getTotalGamesPlayed" -> getTotalGamesPlayed();
            case "placeCard" -> placeCard(actionsInput.getHandIdx());
            case "useAttackHero" -> useAttackHero();
            case "useHeroAbility" -> useHeroAbility();
            default -> throw new IllegalStateException("Unexpected value: " + actionsInput.getCommand());
        };
    }

    private ObjectNode cardUsesAbility(final Coordinates cardAttacker, final Coordinates cardAttacked) {
        String result = game.cardUsesAbility(cardAttacker, cardAttacked);

        if (result != null) {
            ObjectNode resultNode = objectMapper.createObjectNode();

            resultNode.set("cardAttacked", cardAttacked.toObjectNode(objectMapper));
            resultNode.set("cardAttacker", cardAttacker.toObjectNode(objectMapper));
            resultNode.put("command", "cardUsesAttack");
            resultNode.put("error", result);

            return resultNode;
        }

        return null;
    }

    private ObjectNode cardUsesAttack(final Coordinates cardAttacker, final Coordinates cardAttacked) {
        String result = game.cardUsesAttack(cardAttacker, cardAttacked);

        if (result != null) {
            ObjectNode resultNode = objectMapper.createObjectNode();

            resultNode.set("cardAttacked", cardAttacked.toObjectNode(objectMapper));
            resultNode.set("cardAttacker", cardAttacker.toObjectNode(objectMapper));
            resultNode.put("command", "cardUsesAttack");
            resultNode.put("error", result);

            return resultNode;
        }

        return null;
    }

    private ObjectNode endPlayerTurn() {
        game.endPlayerTurn();

        return null;
    }

    private ObjectNode getCardAtPosition(final int x, final int y) {
        ObjectNode resultNode = objectMapper.createObjectNode();

        resultNode.put("command", "getCardAtPosition");
        resultNode.put("x", x);
        resultNode.put("y", y);

        Card card = game.getCardAtPosition(x, y);
        if (card == null) {
            resultNode.put("output", "No card available at that position.");
        } else {
            resultNode.set("output", new CardInput(card).toObjectNode(objectMapper));
        }

        return resultNode;
    }

    private ObjectNode getCardsInHand(final int playerIdx) {
        ObjectNode resultNode = objectMapper.createObjectNode();

        resultNode.put("command", "getCardsInHand");
        resultNode.put("playerIdx", playerIdx);

        ArrayNode handArray = resultNode.putArray("output");

        for (Card card : game.getCardsInHand(playerIdx)) {
            handArray.add(new CardInput(card).toObjectNode(objectMapper));
        }

        return resultNode;
    }

    private ObjectNode getCardsOnTable() {
        ObjectNode resultNode = objectMapper.createObjectNode();

        resultNode.put("command", "getCardsOnTable");
        ArrayNode tableArray = objectMapper.createArrayNode();

        for (ArrayList<Card> row : game.getCardsOnTable()) {
            ArrayNode rowArray = objectMapper.createArrayNode();

            for (Card card : row) {
                rowArray.add(new CardInput(card).toObjectNode(objectMapper));
            }

            tableArray.add(rowArray);
        }

        resultNode.set("output", tableArray);

        return resultNode;
    }

    private ObjectNode getFrozenCardsOnTable() {
        ObjectNode resultNode = objectMapper.createObjectNode();

        resultNode.put("command", "getFrozenCardsOnTable");

        return resultNode;
    }

    private ObjectNode getPlayerDeck(final int playerIdx) {
        ObjectNode resultNode = objectMapper.createObjectNode();

        resultNode.put("command", "getPlayerDeck");
        resultNode.put("playerIdx", playerIdx);

        ArrayNode deckArray = resultNode.putArray("output");

        for (Card card : game.getPlayerDeck(playerIdx)) {
            deckArray.add(new CardInput(card).toObjectNode(objectMapper));
        }

        return resultNode;
    }

    private ObjectNode getPlayerHero(final int playerIdx) {
        ObjectNode resultNode = objectMapper.createObjectNode();

        resultNode.put("command", "getPlayerHero");
        resultNode.put("playerIdx", playerIdx);

        ObjectNode heroNode = new CardInput(game.getPlayerHero(playerIdx)).toObjectNode(objectMapper);
        heroNode.remove("attackDamage");

        resultNode.set("output", heroNode);

        return resultNode;
    }

    private ObjectNode getPlayerMana(final int playerIdx) {
        ObjectNode resultNode = objectMapper.createObjectNode();

        resultNode.put("command", "getPlayerMana");
        resultNode.put("playerIdx", playerIdx);
        resultNode.put("output", game.getPlayerMana(playerIdx));

        return resultNode;
    }

    private ObjectNode getPlayerOneWins() {
        ObjectNode resultNode = objectMapper.createObjectNode();

        resultNode.put("command", "getPlayerOneWins");

        return resultNode;
    }

    private ObjectNode getPlayerTwoWins() {
        ObjectNode resultNode = objectMapper.createObjectNode();

        resultNode.put("command", "getPlayerTwoWins");

        return resultNode;
    }

    private ObjectNode getPlayerTurn() {
        ObjectNode resultNode = objectMapper.createObjectNode();

        resultNode.put("command", "getPlayerTurn");
        resultNode.put("output", game.getPlayerTurn());

        return resultNode;
    }

    private ObjectNode getTotalGamesPlayed() {
        ObjectNode resultNode = objectMapper.createObjectNode();

        resultNode.put("command", "getTotalGamesPlayed");

        return resultNode;
    }

    private ObjectNode placeCard(final int handIdx) {
        String result = game.placeCard(handIdx);

        if (result != null) {
            ObjectNode resultNode = objectMapper.createObjectNode();

            resultNode.put("command", "placeCard");
            resultNode.put("handIdx", handIdx);
            resultNode.put("error", result);

            return resultNode;
        }

        return null;
    }

    private ObjectNode useAttackHero() {
        ObjectNode resultNode = objectMapper.createObjectNode();

        resultNode.put("command", "useAttackHero");

        return resultNode;
    }

    private ObjectNode useHeroAbility() {
        ObjectNode resultNode = objectMapper.createObjectNode();

        resultNode.put("command", "useHeroAbility");

        return resultNode;
    }
}
