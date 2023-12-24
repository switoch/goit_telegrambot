package currency.impl;

import currency.Button;
import currency.Handlers;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class CurrenciesButtonHandler implements Handlers {
    @Override
    public SendMessage sendMessage(Update update) {
        SendMessage message = new SendMessage();
        message.setText("Оберіть потрібні валюти:");
        message.setChatId(update.getCallbackQuery().getMessage().getChatId());

        List<KeyboardRow> keyboards = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton(Button.CURRENCY1.get()));
        keyboardFirstRow.add(new KeyboardButton(Button.CURRENCY2.get()));
        keyboardFirstRow.add(new KeyboardButton(Button.CURRENCY3.get()));

        keyboards.add(keyboardFirstRow);

        ReplyKeyboardMarkup rkm = new ReplyKeyboardMarkup();

        rkm.setKeyboard(keyboards);
        message.setReplyMarkup(rkm);

        return message;
    }
}