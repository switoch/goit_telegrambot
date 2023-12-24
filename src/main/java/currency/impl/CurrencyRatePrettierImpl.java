package currency.impl;

import currency.Currency;
import currency.CurrencyRatePrettier;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class CurrencyRatePrettierImpl implements CurrencyRatePrettier {

    public static final String FORMAT = "Buy rate %s => UAH = %s" + "\n" + "Sale rate %s => UAH = %s";

    @Override
    public String pretty(double rateBuy, double rateSale, Currency ccy) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        return String.format(FORMAT, ccy, df.format(rateBuy), ccy, df.format(rateSale));
    }
}
