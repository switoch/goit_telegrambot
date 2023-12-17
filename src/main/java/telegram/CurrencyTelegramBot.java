package telegram;

import currency.Button;
import currency.Currency;
import currency.CurrencyRatePrettier;
import currency.CurrencyService;
import currency.impl.CurrencyRatePrettierImpl;
import currency.impl.CurrencyServiceImpl;
import currency.impl.CurrencyServiceImplMono;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import telegram.command.StartCommand;

import java.util.Arrays;
import java.util.Collections;
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
    public String getBotUsername() {
        return LoginConstants.NAME;
    }

    @Override
    public String getBotToken() {
        return LoginConstants.TOKEN;
    }

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

            if (data.equals(Button.INFO.get())) {
                String changeLater = Currency.USD.toString();
                getInfoButton(update, changeLater);
            }
            if (data.equals(Button.SETTINGS.get())) {
                settingsButton(update);
            }
        }
    }

    private String getRate(String ccy) {
        Currency currency = Currency.valueOf(ccy);
        return currencyRatePrettier.pretty(currencyService.getRateBuy(currency), currencyService.getRateSale(currency), currency);
    }

    private void getInfoButton(Update update, String currency) {
        String prettyRate = getRate(currency);

        SendMessage sm = new SendMessage();
        sm.setText(prettyRate);
        sm.setChatId(update.getCallbackQuery().getMessage().getChatId());

        InlineKeyboardButton usdButton = InlineKeyboardButton.builder()
                .text(Button.INFO.get())
                .callbackData(Button.INFO.get())
                .build();

        InlineKeyboardButton eurButton = InlineKeyboardButton.builder()
                .text(Button.SETTINGS.get())
                .callbackData(Button.SETTINGS.get())
                .build();

        InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
                .keyboard(Collections.singletonList(Arrays.asList(usdButton, eurButton)))
                .build();

        sm.setReplyMarkup(inlineKeyboardMarkup);

        try {
            execute(sm);
        } catch (TelegramApiException e) {
            System.out.println("something went wrong");
        }
    }

    private void settingsButton(Update update) {

        SendMessage sm = new SendMessage();
        sm.setText("Налаштування");
        sm.setChatId(update.getCallbackQuery().getMessage().getChatId());

        InlineKeyboardButton numSingsButton = InlineKeyboardButton.builder()
                .text(Button.NUMSIGNS.get())
                .callbackData(Button.NUMSIGNS.get())
                .build();
        InlineKeyboardButton bankButton = InlineKeyboardButton.builder()
                .text(Button.BANK.get())
                .callbackData(Button.BANK.get())
                .build();
        InlineKeyboardButton currencyButton = InlineKeyboardButton.builder()
                .text(Button.CURRENCY.get())
                .callbackData(Button.CURRENCY.get())
                .build();
        InlineKeyboardButton timeButton = InlineKeyboardButton.builder()
                .text(Button.TIME.get())
                .callbackData(Button.TIME.get())
                .build();

        InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
                .keyboard(Collections.singletonList(Arrays.asList(numSingsButton, bankButton, currencyButton, timeButton)))
                .build();
        sm.setReplyMarkup(inlineKeyboardMarkup);
        KeyboardButton key = KeyboardButton.builder().build();

        try {
            execute(sm);
        } catch (TelegramApiException e) {
            System.out.println("something went wrong");
        }
    }
}
