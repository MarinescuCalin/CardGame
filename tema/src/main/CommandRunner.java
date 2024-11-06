package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.CardInput;
import game.Card;
import game.Game;

public class CommandRunner {
    private Game game;
    private ObjectMapper objectMapper;

    public CommandRunner(final ObjectMapper objectMapper, final Game game) {
        this.objectMapper = objectMapper;
        this.game = game;
    }

    public ObjectNode executeAction(ActionsInput actionsInput) {
        return switch (actionsInput.getCommand()) {
            case "cardUsesAbility" -> cardUsesAbility();
            case "cardUsesAttack" -> cardUsesAttack();
            case "endPlayerTurn" -> endPlayerTurn();
            case "getCardAtPosition" -> getCardAtPosition();
            case "getCardsInHand" -> getCardsInHand();
            case "getCardsOnTable" -> getCardsOnTable();
            case "getFrozenCardsOnTable" -> getFrozenCardsOnTable();
            case "getPlayerDeck" -> getPlayerDeck(actionsInput.getPlayerIdx());
            case "getPlayerHero" -> getPlayerHero(actionsInput.getPlayerIdx());
            case "getPlayerMana" -> getPlayerMana();
            case "getPlayerOneWins" -> getPlayerOneWins();
            case "getPlayerTwoWins" -> getPlayerTwoWins();
            case "getPlayerTurn" -> getPlayerTurn();
            case "getTotalGamesPlayed" -> getTotalGamesPlayed();
            case "placeCard" -> placeCard();
            case "useAttackHero" -> useAttackHero();
            case "useHeroAbility" -> useHeroAbility();
            default -> throw new IllegalStateException("Unexpected value: " + actionsInput.getCommand());
        };
    }

    private ObjectNode cardUsesAbility() {
        ObjectNode resultNode = objectMapper.createObjectNode();

        resultNode.put("command", "cardUsesAbility");

        return resultNode;
    }

    private ObjectNode cardUsesAttack() {
        ObjectNode resultNode = objectMapper.createObjectNode();

        resultNode.put("command", "cardUsesAttack");

        return resultNode;
    }

    private ObjectNode endPlayerTurn() {
        ObjectNode resultNode = objectMapper.createObjectNode();

        resultNode.put("command", "endPlayerTurn");

        return resultNode;
    }

    private ObjectNode getCardAtPosition() {
        ObjectNode resultNode = objectMapper.createObjectNode();

        resultNode.put("command", "getCardAtPosition");

        return resultNode;
    }

    private ObjectNode getCardsInHand() {
        ObjectNode resultNode = objectMapper.createObjectNode();

        resultNode.put("command", "getCardsInHand");

        return resultNode;
    }

    private ObjectNode getCardsOnTable() {
        ObjectNode resultNode = objectMapper.createObjectNode();

        resultNode.put("command", "getCardsOnTable");

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
            deckArray.add(new CardInput(card).toString());
        }

        return resultNode;
    }

    private ObjectNode getPlayerHero(final int playerIdx) {
        ObjectNode resultNode = objectMapper.createObjectNode();

        resultNode.put("command", "getPlayerHero");
        resultNode.put("playerIdx", playerIdx);

        return resultNode;
    }

    private ObjectNode getPlayerMana() {
        ObjectNode resultNode = objectMapper.createObjectNode();

        resultNode.put("command", "getPlayerMana");

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

        return resultNode;
    }

    private ObjectNode getTotalGamesPlayed() {
        ObjectNode resultNode = objectMapper.createObjectNode();

        resultNode.put("command", "getTotalGamesPlayed");

        return resultNode;
    }

    private ObjectNode placeCard() {
        ObjectNode resultNode = objectMapper.createObjectNode();

        resultNode.put("command", "placeCard");

        return resultNode;
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
