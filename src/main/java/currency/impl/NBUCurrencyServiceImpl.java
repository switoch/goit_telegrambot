package currency.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import currency.Currency;
import currency.CurrencyItem;
import currency.CurrencyService;
import currency.dto.CurrencyItemNbuDto;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class NBUCurrencyServiceImpl implements CurrencyService {
    String url = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json";
    String response;

    {
        try {
            response = Jsoup.connect(url).ignoreContentType(true).get().body().text();
        } catch (IOException e) {
            System.out.println("Помилка під час запиту валюти");
        }
    }

    Type type = TypeToken.getParameterized(List.class, CurrencyItemNbuDto.class).getType();
    List<CurrencyItemNbuDto> list = new Gson().fromJson(response, type);

    @Override
    public CurrencyItem getCurrencyItem(Currency ccy) {
        return list.stream()
                .filter(c -> c.getCc() == ccy)
                .findFirst()
                .orElseThrow();
    }
}
