package currency.dto;

import currency.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyItemDTO {
    private Currency ccy;

    private Currency base_ccy;

    private String buy;

    private String sale;


    public void setCcy(Currency ccy) {
        this.ccy = ccy;
    }

    public void setBase_ccy(Currency base_ccy) {
        this.base_ccy = base_ccy;
    }

    public void setBuy(String buy) {
        this.buy = buy;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }
}
