package currency.dto;

import currency.Bank;
import currency.Currency;
import currency.NotificationTime;
import currency.Precision;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;
@Data
@Getter
@AllArgsConstructor
public class UserSettingsDTO{

    private Bank bank;

    private List<Currency> currency;

    private Precision precision;

    private NotificationTime Time;

    public UserSettingsDTO() {
        this.bank = Bank.PRIVATBANK;
        this.currency = List.of(Currency.USD);
        this.precision = Precision.TWO;
        this.Time = NotificationTime.NO_TIME;
    }

    public void toggleCurrency(Currency ccy) {
        if (this.currency.contains(ccy)) {
            this.currency.remove(ccy);
        } else {
            this.currency.add(ccy);
        }
    }
}
