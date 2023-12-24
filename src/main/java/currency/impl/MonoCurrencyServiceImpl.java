package currency.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import currency.Currency;
import currency.CurrencyItem;
import currency.CurrencyService;
import currency.dto.CurrencyItemMonoDto;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class MonoCurrencyServiceImpl implements CurrencyService {
    String url = "https://api.monobank.ua/bank/currency";
    String response;

    {
        try {
            response = Jsoup.connect(url).ignoreContentType(true).get().body().text();
        } catch (IOException e) {
            System.out.println("Помилка під час запиту валюти");
        }
    }

    Type type = TypeToken.getParameterized(List.class, CurrencyItemMonoDto.class).getType();
    List<CurrencyItemMonoDto> list = new Gson().fromJson(response, type);

    private int toCurrencyCodeAB(Currency ccy) {
        return switch (ccy) {
            case USD -> 840;
            case EUR -> 978;
            case UAH -> 980;
        };
    }

    @Override
    public CurrencyItem getCurrencyItem(Currency ccy) {
        return list.stream()
                .filter(c -> c.getCurrencyCodeA() == toCurrencyCodeAB(ccy))
                .filter(c -> c.getCurrencyCodeB() == toCurrencyCodeAB(Currency.UAH))
                .findFirst()
                .orElseThrow();
    }
}
