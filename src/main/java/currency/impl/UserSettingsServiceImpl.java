package currency.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import currency.dto.UserSettingsDTO;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;


public class UserSettingsServiceImpl {


    private static UserSettingsServiceImpl instance;

    private final HashMap<Long, UserSettingsDTO> store;
    Type type = TypeToken.getParameterized(HashMap.class, UserSettingsDTO.class).getType();

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private UserSettingsServiceImpl() {
        String json = "{}";


        store = gson.fromJson(json, type);

    }

    public static UserSettingsServiceImpl getInstance() {
        if (instance == null) {
            instance = new UserSettingsServiceImpl();
        }
        return instance;
    }

    public void saveSettings(Long chatId, UserSettingsDTO settingsDTO) {
        store.put(chatId, settingsDTO);
        String fileContent = gson.toJson(store, type);
        try (FileWriter writer = new FileWriter("settings.json")) {
            writer.write(fileContent);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public UserSettingsDTO getSettings(Long chatId) {
        return store.getOrDefault(chatId, new UserSettingsDTO());
    }

}
