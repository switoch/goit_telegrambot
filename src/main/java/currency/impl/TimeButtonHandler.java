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

public class TimeButtonHandler implements Handlers {
    @Override
    public SendMessage sendMessage(Update update) {
        SendMessage message = new SendMessage();
        message.setText("Оберіть Час оповіщення:");
        message.setChatId(update.getCallbackQuery().getMessage().getChatId());

        List<KeyboardRow> keyboards = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton(Button.TIME1.get()));
        keyboardFirstRow.add(new KeyboardButton(Button.TIME2.get()));
        keyboardFirstRow.add(new KeyboardButton(Button.TIME3.get()));

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(new KeyboardButton(Button.TIME4.get()));
        keyboardSecondRow.add(new KeyboardButton(Button.TIME5.get()));
        keyboardSecondRow.add(new KeyboardButton(Button.TIME6.get()));

        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardThirdRow.add(new KeyboardButton(Button.TIME7.get()));
        keyboardThirdRow.add(new KeyboardButton(Button.TIME8.get()));
        keyboardThirdRow.add(new KeyboardButton(Button.TIME9.get()));

        KeyboardRow keyboardForthRow = new KeyboardRow();
        keyboardForthRow.add(new KeyboardButton(Button.TIME10.get()));
        keyboardForthRow.add(new KeyboardButton(Button.NOTIME.get()));

        keyboards.add(keyboardFirstRow);
        keyboards.add(keyboardSecondRow);
        keyboards.add(keyboardThirdRow);
        keyboards.add(keyboardForthRow);

        ReplyKeyboardMarkup rkm = new ReplyKeyboardMarkup();

        rkm.setKeyboard(keyboards);
        message.setReplyMarkup(rkm);

        return message;
    }
}
