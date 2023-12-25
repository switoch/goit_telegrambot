package currency;

public enum Bank {

    PRIVATBANK("ПриватБанк"),
    MONO("Моно"),
    NBU("НБУ");

    private final String bankName;
    Bank(String bankName) {
        this.bankName = bankName;
    }
}
