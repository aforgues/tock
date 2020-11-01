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

    //TODO : manage 6 player game type distribution
    private FourPlayerDistributionStage currentDistributionStage = FourPlayerDistributionStage.FIRST;

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

        Iterator<Card> cardIterator = this.cards.iterator();
        while (cardIterator.hasNext()) {
            distributeCardToPlayer(cardIterator.next(), players.get(round++ % nbPlayers));
            cardIterator.remove();

            if (round >= this.currentDistributionStage.getNbCardToDistribute() * nbPlayers) {
                break;
            }
        }

        // plan next distribution
        this.currentDistributionStage = currentDistributionStage.next();
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

    public void play(Card card, Player currentPlayer) {
        this.moveCardFromPlayerToDiscardPile(card, currentPlayer);
        List<Card> playerCards = getPlayerCardHand(currentPlayer);
        playerCards.remove(card);
    }

    public void pass(Player player) {
        Iterator<Card> cardIterator = distributedCardsByPlayer.get(player).iterator();
        while (cardIterator.hasNext()) {
            moveCardFromPlayerToDiscardPile(cardIterator.next(), player);
            cardIterator.remove();
        }
    }

    private void moveCardFromPlayerToDiscardPile(Card card, Player player) {
        if (card == null || player == null) {
            throw new IllegalCardMoveException("Card or player cannot be null");
        }

        List<Card> playerCards = getPlayerCardHand(player);
        if (! playerCards.contains(card)) {
            throw new IllegalCardMoveException("Card " + card.getCardId() + " do not belong to player " + player.getPawnsColor());
        }

        // add card on top (at the beginning) of the list
        discardPile.add(0, card);
    }


}
