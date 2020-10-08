package org.aforgues.tock.domain;

import lombok.Getter;
import lombok.ToString;

import java.util.*;

@ToString
public class CardDeck {
    private final List<Card> cards;
    private Map<Player, List<Card>> distributedCardsByPlayer;
    @Getter
    private List<Card> discardPile;

    public CardDeck() {
        this.cards = new ArrayList<>();
        this.distributedCardsByPlayer = new HashMap<>();
        this.discardPile = new ArrayList<>();

        Arrays.stream(Card.CardColor.values())
                .forEach(cardColor -> Arrays.stream((Card.CardValue.values()))
                        .forEach(cardValue -> this.cards.add(new Card(cardValue, cardColor ))));
        this.shuffle();
    }

    private void shuffle() {
        Collections.shuffle(this.cards);
    }

    public void distributeToPlayers(List<Player> players) {
        final int nbPlayers = players.size();

        int round = 0;

        for (Card card : this.cards) {
            distributeCardToPlayer(card, players.get(round++ % nbPlayers));
        }
    }

    private void distributeCardToPlayer(Card card, Player player) {
        List<Card> playerCards = distributedCardsByPlayer.get(player);
        if (playerCards == null) {
            playerCards = new ArrayList<>();
            distributedCardsByPlayer.put(player, playerCards);
        }
        playerCards.add(card);
    }

    public List<Card> getPlayerCardHand(Player player) {
        return this.distributedCardsByPlayer.get(player);
    }

    public void moveCardFromPlayerToDiscardPile(Card card, Player player) {
        if (card == null || player == null) {
            throw new IllegalCardMoveException("Card or player cannot be null");
        }

        List<Card> playerCards = getPlayerCardHand(player);
        if (! playerCards.contains(card)) {
            throw new IllegalCardMoveException("Card " + card.getCardId() + " do not belong to player " + player.getPawnsColor());
        }

        // add card on top (at the beginning) of the list
        discardPile.add(0, card);
        playerCards.remove(card);
    }

}
