package sockn.gamelogic;

import sockn.enums.CardColor;
import sockn.enums.CardSymbol;
import sockn.exceptions.SocknException;

import java.util.Collections;
import java.util.EmptyStackException;
import java.util.Stack;

public class CardStack {
    public static final String NO_MORE_CARDS = "No more cards!";
    private Stack<Card> cards;

    public CardStack() {
        initStack();
    }

    private void initStack() {

        cards = new Stack<>();

        for(CardColor color : CardColor.values()) {
            for(CardSymbol symbol : CardSymbol.values()) {
                cards.push(new Card(color, symbol));
            }
        }
        Collections.shuffle(cards);
    }

    Card getCard() throws SocknException {
        try {
            return cards.pop();
        } catch (EmptyStackException e) {
            throw new SocknException(NO_MORE_CARDS);
        }
    }

    Card showLatestCard() throws SocknException {
        try {
            return cards.peek();
        } catch (EmptyStackException e) {
            throw new SocknException(NO_MORE_CARDS);
        }
    }
}
