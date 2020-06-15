package org.aforgues.tock.domain;

import lombok.ToString;

import java.util.*;

@ToString
public class CardDeck {
    private final List<Card> cards;
    private Map<Player, List<Card>> distributedCardsByPlayer;

    public CardDeck() {
        this.cards = new ArrayList<>();
        this.distributedCardsByPlayer = new HashMap<>();

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
}
