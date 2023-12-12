package currency.impl;

import currency.Currency;

public class CSTest {
    public static void main(String[] args) {
        CurrencyServiceImpl currencyService = new CurrencyServiceImpl();
        System.out.println("currencyService = " + currencyService.getRate(Currency.USD));
    }
}
