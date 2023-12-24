package currency.dto;

import currency.CurrencyItem;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyItemMonoDto implements CurrencyItem {
    private int currencyCodeA;
    private int currencyCodeB;
    private double rateBuy;
    private double rateSell;
    private double rateCross;
    private int date;

    @Override
    public Double getSellRate() {
        return rateSell;
    }

    @Override
    public Double getBuyRate() {
        return rateBuy;
    }
}
