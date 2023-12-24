package telegram;

import currency.*;
import currency.impl.*;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import telegram.command.StartCommand;

public class CurrencyTelegramBot extends TelegramLongPollingCommandBot {

    private final CurrencyService currencyService;
    private final CurrencyRatePrettier currencyRatePrettier;

    public CurrencyTelegramBot() {
        //currencyService = new CurrencyServiceImpl();
        currencyService = new MonoCurrencyServiceImpl();
        currencyRatePrettier = new CurrencyRatePrettierImpl();
        register(new StartCommand());
    }

    @Override
    public String getBotUsername() {
        return Credentials.NAME;
    }

    @Override
    public String getBotToken() {
        return Credentials.TOKEN;
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
        CurrencyItem currencyItem = currencyService.getCurrencyItem(currency);
        return currencyRatePrettier.pretty(currencyItem.getBuyRate(), currencyItem.getSellRate(), currency);
    }
}
