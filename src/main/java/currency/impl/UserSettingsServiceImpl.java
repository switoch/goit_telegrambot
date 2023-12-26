package currency.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import currency.dto.UserSettingsDTO;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class UserSettingsServiceImpl {


    private static UserSettingsServiceImpl instance;

    private final Map<Long, UserSettingsDTO> store;
    private final Type type = new TypeToken<Map<Long, UserSettingsDTO>>(){}.getType();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private UserSettingsServiceImpl() {
        String json = "{}";
        File file = new File("settings.json");
        List<String> rows = new ArrayList<>();
        if (file.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                json = br.lines().collect(Collectors.joining("\n"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public UserSettingsDTO getSettings(Long chatId) {
        return store.getOrDefault(chatId, new UserSettingsDTO());
    }

}
