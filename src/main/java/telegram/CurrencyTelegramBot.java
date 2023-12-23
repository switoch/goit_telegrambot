package telegram;

import currency.Button;
import currency.Currency;
import currency.CurrencyRatePrettier;
import currency.CurrencyService;
import currency.impl.*;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import telegram.command.StartCommand;

import java.util.List;
import java.util.stream.Collectors;

public class CurrencyTelegramBot extends TelegramLongPollingCommandBot {

    private CurrencyService currencyService;
    private CurrencyRatePrettier currencyRatePrettier;
    public CurrencyTelegramBot() {
        //currencyService = new CurrencyServiceImpl();
        currencyService = new CurrencyServiceImplMono();
        currencyRatePrettier = new CurrencyRatePrettierImpl();
        register(new StartCommand());
    }

    @Override
    public String getBotUsername(){
        return LoginConstants.NAME;
    }

    @Override
    public String getBotToken(){
        return LoginConstants.TOKEN;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasMessage()) {
            SendMessage message = new MessageHandler().sendMessage(update);
            try {
                execute(message);
            } catch (TelegramApiException e) {
                System.out.println("Щось пішло не так...");
            }
        }

        if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            String prettyRate = getRate(data);

            SendMessage sm = new SendMessage();
            sm.setText(prettyRate);
            sm.setChatId(update.getCallbackQuery().getMessage().getChatId());

            try {
                execute(sm);
            } catch (TelegramApiException e) {
                System.out.println("something went wrong");
            }

            if (data.equals(Button.TIME.get())) {
                SendMessage message = new TimeButtonHandler().sendMessage(update);

                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    System.out.println("Щось пішло не так...");
                }
            }
        }
    }

    private String getRate(String ccy) {
        Currency currency = Currency.valueOf(ccy);
        return currencyRatePrettier.pretty(currencyService.getRateBuy(currency), currencyService.getRateSale(currency), currency);
    }
}
