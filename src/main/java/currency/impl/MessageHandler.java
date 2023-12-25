package currency.impl;

import currency.Button;
import currency.Handlers;
import currency.NotificationTime;
import currency.dto.UserSettingsDTO;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MessageHandler implements Handlers {
    @Override
    public SendMessage sendMessage(Update update) {
        String receivedText = update.getMessage().getText();
        String text = "";
        List<String> buttonsTime = Arrays.stream(NotificationTime.values()).map(NotificationTime::get).toList();
        List<String> buttonsNumSigns = Arrays.asList(Button.SIGNS1.get(), Button.SIGNS2.get(), Button.SIGNS3.get());
        List<String> banks = Arrays.asList(Button.BANK1.get(), Button.BANK2.get(), Button.BANK3.get());
        List<String> currencies = Arrays.asList(Button.CURRENCY1.get(), Button.CURRENCY2.get(), Button.CURRENCY3.get());

        SendMessage message = new SendMessage();
        UserSettingsServiceImpl settingsService = UserSettingsServiceImpl.getInstance();
        UserSettingsDTO userSettings = settingsService.getSettings(update.getMessage().getChatId());
        if (receivedText.equals(Button.NOTIME.get())) {
            text = "Ви вимкнули час сповіщеннь, тому інформацію можете отримувати натискаючи кнопку Отримати інфо";
        }
        for (String button : buttonsTime) {
            if (receivedText.equals(button)) {
                userSettings.setTime(NotificationTime.getEnum(receivedText));
                text = "Ви обрали час надсилання повідомлень о " + receivedText;
            }
        }

        for (String button : buttonsNumSigns) {
            if (receivedText.equals(button)) {
                text = "Ви обрали кількість знаків після коми " + receivedText;
                break;
            }
        }
        for (String button : banks) {
            if (receivedText.equals(button)) {
                text = "Ви обрали банк: " + receivedText;
                break;
            }
        }
        for (String button : currencies) {
            if (receivedText.equals(button)) {
                text = "З валют Ви обрали: " + receivedText;
                break;
            }
        }

        if (text.isEmpty()) {
            text = "Ви написали: " + receivedText + " Цей бот не має можливості опрацьовувати текст. Оберіть, будь ласка, кнопку";
        }
        message.setText(text);
        message.setChatId(update.getMessage().getChatId());

        settingsService.saveSettings(update.getMessage().getChatId(), userSettings);
        return message;
    }
}
