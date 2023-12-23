package currency.impl;

import currency.Button;
import currency.Handlers;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.List;

public class MessageHandler implements Handlers {
    @Override
    public SendMessage sendMessage(Update update) {
        String receivedText = update.getMessage().getText();
        String text = "";
        List<String> buttonsTime = Arrays.asList(Button.TIME1.get(), Button.TIME2.get(), Button.TIME3.get(), Button.TIME4.get(), Button.TIME5.get(), Button.TIME6.get(), Button.TIME7.get(), Button.TIME8.get(), Button.TIME9.get(), Button.TIME10.get());
        List<String> buttonsNumSigns = Arrays.asList(Button.SIGNS1.get(),Button.SIGNS2.get(),Button.SIGNS3.get());
        List<String> banks = Arrays.asList(Button.BANK1.get(),Button.BANK2.get(),Button.BANK3.get());
        List<String> currencies = Arrays.asList(Button.CURRENCY1.get(),Button.CURRENCY2.get(),Button.CURRENCY3.get());
        SendMessage message = new SendMessage();
        if (receivedText.equals(Button.NOTIME.get())) {
            text = "Ви вимкнули час сповіщеннь, тому інформацію можете отримувати натискаючи кнопку Отримати інфо";
        }
        for (String button: buttonsTime) {
            if (receivedText.equals(button)) {
                text = "Ви обрали час надсилання повідомлень о " + receivedText;
            }
        }
        for (String button: buttonsNumSigns){
            if (receivedText.equals(button)) {
                text = "Ви обрали кількість знаків після коми " + receivedText;
            }
        }
        for (String button: banks){
            if (receivedText.equals(button)) {
                text = "Ви обрали банк: " + receivedText;
            }
        }
        for (String button: currencies){
            if (receivedText.equals(button)) {
                text = "З валют Ви обрали: " + receivedText;
            }
        }
        if (text.isEmpty()) {
            text = "Ви написали: " + receivedText + " Цей бот не має можливості опрацьовувати текст. Оберіть, будь ласка, кнопку";
        }
        message.setText(text);
        message.setChatId(update.getMessage().getChatId());

        return message;
    }
}
