package currency.impl;

import currency.Bank;
import currency.Button;
import currency.Precision;
import currency.dto.UserSettingsDTO;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PrecisionButtonHandler {
    private static PrecisionButtonHandler instance;
    public static PrecisionButtonHandler getInstance() {
        if (instance == null) {
            instance = new PrecisionButtonHandler();
        }
        return instance;

    }
    private PrecisionButtonHandler() {
    }

    private final UserSettingsServiceImpl settingsService = UserSettingsServiceImpl.getInstance();

    private String getButtonCaption(Precision precision, Precision selectedPrecision) {
        String active = "✅";
        if (precision == selectedPrecision) {
            return active + precision.getValue();
        }
        return String.valueOf(precision.getValue());
    }

    private InlineKeyboardMarkup getKeyboard(Long chatId) {
        Precision selectedPrecision = settingsService.getSettings(chatId).getPrecision();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        keyboard.add(
                Arrays.stream(Precision.values()).map(precision -> {
                    return InlineKeyboardButton.builder()
                            .text(getButtonCaption(precision, selectedPrecision))
                            .callbackData(String.valueOf(precision))
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
        message.setText("Оберіть кількість знаків після коми");
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
        userSettings.setPrecision(Precision.valueOf(data));
        settingsService.saveSettings(chatId, userSettings);
        return EditMessageReplyMarkup.builder()
                .chatId(chatId)
                .messageId(messageId)
                .replyMarkup(getKeyboard(chatId))
                .build();
    }
}
