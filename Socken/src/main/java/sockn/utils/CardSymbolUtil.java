package sockn.utils;

import sockn.enums.CardSymbol;

public class CardSymbolUtil {

    public static int getValue(CardSymbol symbol, boolean isTrump) {
        final int value = symbol.getValue();

        if (isTrump) {
            if (value == 9)
                return 15;
            if (value == 11)
                return 16;
        }

        return value;
    }

    public static int getPoints(CardSymbol symbol, boolean isTrump) {
        final int value = symbol.getValue();

        if (isTrump) {
            if (value == 9) {
                return 14;
            } else if (value == 11) {
                return 20;
            }
        }

        if (between(6, 9, value))
            return 0;

        if (between(11, 14, value))
            return value - 9;

        return value;
    }

    private static boolean between(int min, int max, int value) {
        return value >= min && value <= max;
    }
}
