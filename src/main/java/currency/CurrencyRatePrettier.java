package currency;

public interface CurrencyRatePrettier {
    String pretty(double rateBuy, double rateSale, Currency ccy);
}
