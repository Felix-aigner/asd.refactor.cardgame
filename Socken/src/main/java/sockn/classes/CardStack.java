package sockn.classes;

import sockn.enums.CardColor;
import sockn.enums.CardSymbol;
import sockn.exceptions.SocknException;

import java.util.Collections;
import java.util.EmptyStackException;
import java.util.Stack;

public class CardStack {
    private Stack<Card> cards;

    public CardStack() {
        initStack();
    }

    private void initStack() {
        Card[] cardNames = new Card[36];

        // init stack
        cards = new Stack<>();

        // generate card names
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
            throw new SocknException("No more cards!");
        }
    }

    Card showLatestCard() throws SocknException {
        try {
            return cards.peek();
        } catch (EmptyStackException e) {
            throw new SocknException("No more cards!");
        }
    }
}
