package telegram;

import currency.*;
import currency.dto.UserSettingsDTO;
import currency.impl.*;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import telegram.command.StartCommand;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class CurrencyTelegramBot extends TelegramLongPollingCommandBot {

    private CurrencyRatePrettier currencyRatePrettier;


    public CurrencyTelegramBot() {
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
            handleMessage(update);
        }

        if (update.hasCallbackQuery()) {
            handleCallback(update);
        }

    }

    private void handleMessage(Update update) {
        SendMessage message = new MessageHandler().sendMessage(update);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println("Щось пішло не так...");
        }
    }

    private void handleCallback(Update update) {
        String data = update.getCallbackQuery().getData();
        if (data.equals(Button.INFO.get())) {
            getInfoButton(update);
        }
        if (data.equals(Button.SETTINGS.get())) {
            settingsButton(update);
        }
        if (data.equals(Button.NUMSIGNS.get())) {
            SendMessage message = PrecisionButtonHandler.getInstance().createMessage(update);

            try {
                execute(message);
            } catch (TelegramApiException e) {
                System.out.println("Щось пішло не так...");
            }
        }
        if (data.equals(Button.TIME.get())) {
            SendMessage message = new TimeButtonHandler().sendMessage(update);

            try {
                execute(message);
            } catch (TelegramApiException e) {
                System.out.println("Щось пішло не так...");
            }
        }
        if (data.equals(Button.BANK.get())) {
            SendMessage message = BankButtonHandler.getInstance().createMessage(update);
            try {
                execute(message);
            } catch (TelegramApiException e) {
                System.out.println("Щось пішло не так...");
            }
        }
        if (data.equals(Button.CURRENCY.get())) {
            SendMessage message = CurrenciesButtonHandler.getInstance().createMessage(update);
            try {
                execute(message);
            } catch (TelegramApiException e) {
                System.out.println("Щось пішло не так...");
            }
        }
        if (data.equals(String.valueOf(Currency.USD)) || data.equals(String.valueOf(Currency.EUR))) {
            try {
                execute(CurrenciesButtonHandler.getInstance().editMessage(update));
            } catch (TelegramApiException e) {
                System.out.println("Щось пішло не так...");
            }
        }
        if (Arrays.stream(Bank.values()).map(String::valueOf).toList().contains(data)) {
            try {
                execute(BankButtonHandler.getInstance().editMessage(update));
            } catch (TelegramApiException e) {
                System.out.println("Щось пішло не так...");
            }
        }
        if (Arrays.stream(Precision.values()).map(String::valueOf).toList().contains(data)) {
            try {
                execute(PrecisionButtonHandler.getInstance().editMessage(update));
            } catch (TelegramApiException e) {
                System.out.println("Щось пішло не так...");
            }
        }
    }


    private String getRate(UserSettingsDTO userSettings) {
        CurrencyService cs = null;
        switch (userSettings.getBank()) {
            case PRIVATBANK -> cs = new PrivateBankCurrencyServiceImpl();
            case MONO -> cs = new MonoCurrencyServiceImpl();
            case NBU -> cs = new NBUCurrencyServiceImpl();
        }

        CurrencyService finalCs = cs;
        return String.format(
                "%s:\n%s",
                userSettings.getBank().getBankName(),
                !userSettings.getCurrency().isEmpty() ? userSettings.getCurrency().stream().map(currency -> {
            CurrencyItem ci = finalCs.getCurrencyItem(currency);
            return currencyRatePrettier.pretty(ci.getBuyRate(), ci.getSellRate(), currency, userSettings.getPrecision().getValue());
        }).collect(Collectors.joining("\n\n")) : "Не обрано валу для відображення");
    }

    private void getInfoButton(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        UserSettingsDTO userSettings = UserSettingsServiceImpl.getInstance().getSettings(chatId);
        String prettyRate = getRate(userSettings);
        SendMessage sm = new SendMessage();
        sm.setText(prettyRate);

        sm.setChatId(chatId);

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

        try {
            execute(sm);
        } catch (TelegramApiException e) {
            System.out.println("something went wrong");
        }
    }
}
