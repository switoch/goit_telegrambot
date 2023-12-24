package currency.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import currency.Currency;
import currency.CurrencyItem;
import currency.CurrencyService;
import currency.dto.CurrencyItemPrivateBankDto;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class PrivateBankCurrencyServiceImpl implements CurrencyService {
    String url = "https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5";
    String jsonString;

    {

        try {
            jsonString = Jsoup.connect(url)
                    .ignoreContentType(true)
                    .get()
                    .body()
                    .text();
        } catch (IOException e) {
            System.out.println("error while currency request");
        }
    }

    Type type = TypeToken.getParameterized(List.class, CurrencyItemPrivateBankDto.class)
            .getType();
    List<CurrencyItemPrivateBankDto> list = new Gson().fromJson(jsonString, type);

    @Override
    public CurrencyItem getCurrencyItem(Currency ccy) {
        return list.stream()
                .filter(c -> c.getCcy() == ccy)
                .filter(c -> c.getBase_ccy() == Currency.UAH)
                .findFirst()
                .orElseThrow();
    }
}
