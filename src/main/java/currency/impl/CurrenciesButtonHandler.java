package currency.impl;

import currency.Button;
import currency.Currency;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CurrenciesButtonHandler {
    private static CurrenciesButtonHandler instance;
    public static CurrenciesButtonHandler getInstance() {
        if (instance == null) {
            instance = new CurrenciesButtonHandler();
        }
        return instance;

    }
    private CurrenciesButtonHandler() {
    }

    private final UserSettingsServiceImpl settingsService = UserSettingsServiceImpl.getInstance();

    private String getButtonCaption(Currency ccy, List<Currency> userSettingsCurrencies) {
        String active = "✅";
        if (userSettingsCurrencies.contains(ccy)) {
            return active + String.valueOf(ccy);
        }
        return String.valueOf(ccy);
    }

    private InlineKeyboardMarkup getCurrencyKeyboard(Long chatId) {
        List<Currency> currencies = settingsService.getSettings(chatId).getCurrency();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        keyboard.add(
                Arrays.asList(
                        InlineKeyboardButton.builder()
                                .text(getButtonCaption(Currency.USD, currencies))
                                .callbackData(String.valueOf(Currency.USD))
                                .build(),
                        InlineKeyboardButton.builder()
                                .text(getButtonCaption(Currency.EUR, currencies))
                                .callbackData(String.valueOf(Currency.EUR))
                                .build()
                )
        );
        keyboard.add(
                Collections.singletonList(
                        InlineKeyboardButton.builder()
                                .text(Button.SETTINGS.get())
                                .callbackData(Button.SETTINGS.get())
                                .build()
                )
        );

        return InlineKeyboardMarkup.builder().keyboard(keyboard).build();
    }

    public SendMessage createMessage(Update update) {
        SendMessage message = new SendMessage();
        message.setText("Оберіть потрібні валюти:");
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        message.setChatId(chatId);
        message.setReplyMarkup(getCurrencyKeyboard(chatId));
        return message;
    }

    public EditMessageReplyMarkup editMessage(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        int messageId = update.getCallbackQuery().getMessage().getMessageId();

        String data = update.getCallbackQuery().getData();
        settingsService.getSettings(chatId).toggleCurrency(Currency.valueOf(data));
        settingsService.saveSettings(chatId, settingsService.getSettings(chatId));
        return EditMessageReplyMarkup.builder()
                .chatId(chatId)
                .messageId(messageId)
                .replyMarkup(getCurrencyKeyboard(chatId))
                .build();
    }
}