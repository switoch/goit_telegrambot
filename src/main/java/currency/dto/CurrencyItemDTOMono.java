package currency.dto;

import currency.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyItemDTOMono {
    private int currencyCodeA;
    private int currencyCodeB;
    private double rateBuy;
    private double rateSell;
    private double rateCross;
    private int date;

}
