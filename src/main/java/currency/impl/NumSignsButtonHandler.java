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

public class NumSignsButtonHandler implements Handlers {
    @Override
    public SendMessage sendMessage(Update update) {
        SendMessage message = new SendMessage();
        message.setText("Оберіть кількість знаків після коми:");
        message.setChatId(update.getCallbackQuery().getMessage().getChatId());

        List<KeyboardRow> keyboards = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton(Button.SIGNS1.get()));
        keyboardFirstRow.add(new KeyboardButton(Button.SIGNS2.get()));
        keyboardFirstRow.add(new KeyboardButton(Button.SIGNS3.get()));

        keyboards.add(keyboardFirstRow);

        ReplyKeyboardMarkup rkm = new ReplyKeyboardMarkup();

        rkm.setKeyboard(keyboards);
        message.setReplyMarkup(rkm);

        return message;
    }
}
