package telegram;

import currency.Currency;
import currency.CurrencyRatePrettier;
import currency.CurrencyService;
import currency.impl.CurrencyRatePrettierImpl;
import currency.impl.CurrencyServiceImpl;
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
        currencyService = new CurrencyServiceImpl();
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

//    @Override
//    public void onUpdatesReceived(List<Update> updates){
//        updates.stream()
//                .filter(u ->u.hasMessage())
//                .map(u -> u.getMessage().getText() + "form" + u.getMessage().getChat().getUserName())
//                .peek(System.out::println)
//                .collect(Collectors.toList());
//        System.out.println("Some updsa");
//
//    }


    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasMessage()) {
            String receivedText = update.getMessage().getText();

            SendMessage sm = new SendMessage();
            sm.setText("You just wrote " + receivedText);
            sm.setChatId(update.getMessage().getChatId());

            try {
                execute(sm);
            } catch (TelegramApiException e) {
                System.out.println("something went wrong");
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
        }
    }

    private String getRate(String ccy) {
        Currency currency = Currency.valueOf(ccy);
        return currencyRatePrettier.pretty(currencyService.getRate(currency), currency);
    }
}
