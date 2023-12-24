package currency.dto;

import currency.Currency;
import currency.CurrencyItem;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyItemPrivateBankDto implements CurrencyItem {
    private Currency ccy;
    private Currency base_ccy;
    private String buy;
    private String sale;

    @Override
    public Double getSellRate() {
        return Double.parseDouble(sale);
    }

    @Override
    public Double getBuyRate() {
        return Double.parseDouble(buy);
    }
}
