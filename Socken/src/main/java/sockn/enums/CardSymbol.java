package sockn.enums;

public enum CardSymbol {
    SECHS(6),
    SIEBEN(7),
    ACHT(8),
    NEUN(9),
    ZEHN(10),
    UNTER(11), //Bauer
    OBER(12), //Dame
    KOENIG(13),
    ASS(14);

    private final int value;

    CardSymbol(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
