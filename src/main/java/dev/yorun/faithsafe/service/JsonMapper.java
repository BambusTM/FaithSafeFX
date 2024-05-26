package dev.yorun.faithsafe.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonMapper {
    private static final String FILE_PATH = "data.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void saveToJson(String username, String domain, String email, String password, String description) {
        List<DataObject> entries = loadFromJson();
        DataObject newEntry = new DataObject(username, domain, email, password, description);
        entries.add(newEntry);
        saveToFile(entries);
    }

    public List<DataObject> loadFromJson() {
        try {
            File file = new File(FILE_PATH);
            if (file.exists()) {
                return objectMapper.readValue(file, new TypeReference<List<DataObject>>() {});
            }
        } catch (IOException e) {
            System.err.println("Failed to load existing data: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    private void saveToFile(List<DataObject> entries) {
        try {
            objectMapper.writeValue(new File(FILE_PATH), entries);
        } catch (IOException e) {
            System.err.println("Failed to save data: " + e.getMessage());
        }
    }
}
