package sockn.enums;

public enum CardSymbol {
    SECHS   (6),
    SIEBEN  (7),
    ACHT    (8),
    NEUN    (9),
    ZEHN    (10),
    UNTER   (11),
    OBER    (12),
    KOENIG  (13),
    ASS     (14),
    ;

    private final int value;

    CardSymbol(int value) {
        this.value = value;
    }

    public int getValue(boolean isTrump) {
        if(isTrump) {
            if(this.value == 9) {
                return 15;
            } else if (this.value == 11) {
                return 16;
            }
        }
        return this.value;
    }

    public int getPoints(boolean isTrump) {
        if (isTrump) {
            if (this.value == 9) {
                return 14;
            } else if (this.value == 11) {
                return 20;
            }
        }

        if(this.value == 6 || this.value == 7 || this.value == 8 || this.value == 9) {
            return 0;
        } else if (this.value == 10) {
            return 10;
        } else if (this.value == 11) {
            return 2;
        } else if (this.value == 12) {
            return 3;
        } else if (this.value == 13) {
            return 4;
        } else if (this.value == 14) {
            return 11;
        }

        return this.value;
    }
}
