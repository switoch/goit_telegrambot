package currency;

public enum Bank {

    PRIVATBANK("ПриватБанк"),
    MONO("Моно"),
    NBU("НБУ");

    private final String bankName;
    Bank(String bankName) {
        this.bankName = bankName;
    }

    public String getBankName() {
        return bankName;
    }

    public static Bank resolveEnumByStr(String bankName) {
        for (Bank bank: Bank.values()) {
            if (bank.getBankName().equals(bankName)) {
                return bank;
            }
        }
        return null;
    }
}
