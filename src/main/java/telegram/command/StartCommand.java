package telegram.command;


import currency.Button;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class StartCommand extends BotCommand {
    public StartCommand() {
        super("start", "start command will initiate currency bot");
    }


    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String text = "Ласкаво просимо. Цей бот допоможе відслідковувати актуальні курси валют";

        SendMessage sm = new SendMessage();
        sm.setText(text);
        sm.setChatId(chat.getId());

        InlineKeyboardButton usdButton = InlineKeyboardButton.builder()
                .text(Button.INFO.get())
                .callbackData(Button.INFO.get())
                .build();

        InlineKeyboardButton eurButton = InlineKeyboardButton.builder()
                .text(Button.SETTINGS.get())
                .callbackData(Button.SETTINGS.get())
                .build();

        InlineKeyboardMarkup inlineKeyboardMarkup =  InlineKeyboardMarkup.builder()
                .keyboard(Collections.singletonList(Arrays.asList(usdButton, eurButton)))
                .build();

        sm.setReplyMarkup(inlineKeyboardMarkup);
        try {
            absSender.execute(sm);
        } catch (TelegramApiException e) {
            System.out.println("Помилка!");
        }
    }
}
