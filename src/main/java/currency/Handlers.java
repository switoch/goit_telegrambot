package currency;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Handlers {
    SendMessage sendMessage(Update update);
}
