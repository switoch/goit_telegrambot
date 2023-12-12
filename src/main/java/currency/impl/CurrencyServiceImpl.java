package currency.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import currency.Currency;
import currency.CurrencyService;
import currency.dto.CurrencyItemDTO;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class CurrencyServiceImpl implements CurrencyService {
    public double getRate(Currency ccy){
        String url = "https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5";
       String jsonString = "";

        try {
            jsonString = Jsoup.connect(url)
                    .ignoreContentType(true)
                    .get()
                    .body()
                    .text();
        } catch (IOException e) {
            System.out.println("error while currency request");
        }

        Type type = TypeToken.getParameterized(List.class, CurrencyItemDTO.class)
                .getType();
        List<CurrencyItemDTO> list =  new Gson().fromJson(jsonString, type);
       String s = list.stream()
               .filter(c -> c.getCcy() == ccy)
               .filter(c -> c.getBase_ccy() == Currency.UAH)
               .map(c ->c.getBuy())
               .findFirst()
               .orElseThrow();

        return Double.valueOf(s);
    }
}
