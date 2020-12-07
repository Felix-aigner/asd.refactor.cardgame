package sockn.classes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sockn.enums.CardColor;
import sockn.enums.CardSymbol;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoundTest {
    private static final int PLAYER_COUNT = 4;
    private Player[] players;
    private Round round;
    private Card[] cardsToPlay = new Card[] {
            new Card(CardColor.HERZ, CardSymbol.ZEHN),
            new Card(CardColor.PIK, CardSymbol.SECHS),
            new Card(CardColor.PIK, CardSymbol.KOENIG),
            new Card(CardColor.KARO, CardSymbol.ASS),
    };

    @BeforeEach
    void setUp() {
        // create players
        players = new Player[PLAYER_COUNT];
        players[0] = new Player(0, "David", true, null);
        for (int i = 1; i < PLAYER_COUNT; i++) {
            players[i] = new Player(i, Constants.COMP_NAMES[i - 1], false, null);
        }

        // create round
        round = new Round(1, null, players, null);

        // mix cards
        round.mixCards();

        // distribute cards
        round.distributeCardsToPlayers();
    }

    @Test
    void distributeCardsToPlayers() {
        // expected cards should be 9
        int expected = 9;

        // assert for each player that has 9 cards
        for(Player player : players) {
            int actual = player.getHandCards().size();
            assertEquals(expected, actual);
        }
    }

    @Test
    void determineTrumpForFourPlayers() {
        // determine Trump
        round.determineTrump();

        // trump determined by the test function
        CardColor trump = round.getTrump();

        // trump has to be the last card if 4 players joined the game
        ArrayList<Card> playerHandCards = players[3].getHandCards();
        Card lastCard = playerHandCards.get(playerHandCards.size() - 1);

        assertEquals(trump, lastCard.getColor());
    }

    @Test
    void determineStichWithPikAsTrump() {
        // play card
        for(int i = 0; i < PLAYER_COUNT; i++) {
            round.addPlayedCard(players[i], cardsToPlay[i]);
        }

        // manually define trump
        // round.trump = CardColor.PIK;

        // determine stich
        int[] playerIndexAndPoints = round.determineStich();

        // index of player that got the stich
        int actualPlayerIndex = playerIndexAndPoints[0];
        int expectedPlayerIndex = 2;

        assertEquals(actualPlayerIndex, expectedPlayerIndex);
    }

    @Test
    void determineStichWithHerzAsTrump() {
        // play card
        for(int i = 0; i < PLAYER_COUNT; i++) {
            round.addPlayedCard(players[i], cardsToPlay[i]);
        }

        // manually define trump
        // round.trump = CardColor.HERZ;

        // determine stich
        int[] playerIndexAndPoints = round.determineStich();

        // index of player that got the stich
        int actualPlayerIndex = playerIndexAndPoints[0];
        int expectedPlayerIndex = 0;

        assertEquals(actualPlayerIndex, expectedPlayerIndex);
    }
}