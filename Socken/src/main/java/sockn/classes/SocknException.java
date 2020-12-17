package sockn.classes;

class SocknException extends Throwable {
    SocknException(String no_more_cards) {
        super(no_more_cards);
    }
}
