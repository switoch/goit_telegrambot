package currency.impl;

import currency.Bank;
import currency.Button;
import currency.Currency;
import currency.Handlers;
import currency.dto.UserSettingsDTO;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BankButtonHandler {
    private static BankButtonHandler instance;
    public static BankButtonHandler getInstance() {
        if (instance == null) {
            instance = new BankButtonHandler();
        }
        return instance;

    }
    private BankButtonHandler() {
    }

    private final UserSettingsServiceImpl settingsService = UserSettingsServiceImpl.getInstance();

    private String getButtonCaption(Bank ccy, Bank selectedBank) {
        String active = "✅";
        if (ccy == selectedBank) {
            return active + ccy.getBankName();
        }
        return ccy.getBankName();
    }

    private InlineKeyboardMarkup getKeyboard(Long chatId) {
        Bank selectedBank = settingsService.getSettings(chatId).getBank();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        keyboard.add(
                Arrays.stream(Bank.values()).map(bank -> {
                    return InlineKeyboardButton.builder()
                            .text(getButtonCaption(bank, selectedBank))
                            .callbackData(String.valueOf(bank))
                            .build();
                }).toList()
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
        message.setText("Оберіть Банк:");
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        message.setChatId(chatId);
        message.setReplyMarkup(getKeyboard(chatId));
        return message;
    }

    public EditMessageReplyMarkup editMessage(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        int messageId = update.getCallbackQuery().getMessage().getMessageId();
        UserSettingsDTO userSettings = settingsService.getSettings(chatId);
        String data = update.getCallbackQuery().getData();
        userSettings.setBank(Bank.valueOf(data));
        settingsService.saveSettings(chatId, userSettings);
        return EditMessageReplyMarkup.builder()
                .chatId(chatId)
                .messageId(messageId)
                .replyMarkup(getKeyboard(chatId))
                .build();
    }
}


