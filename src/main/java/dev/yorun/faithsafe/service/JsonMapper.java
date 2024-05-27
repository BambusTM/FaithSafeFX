package dev.yorun.faithsafe.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.yorun.faithsafe.objects.DataObject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonMapper {
    private static final String FILE_PATH = "data.json";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private int currentMaxId = 0;

    public JsonMapper() {
        List<DataObject> entries = loadFromJson();
        if (!entries.isEmpty()) {
            currentMaxId = entries.stream().mapToInt(DataObject::getId).max().orElse(0);
        }
    }

    public void saveToJson(String username, String domain, String email, String password, String description) {
        List<DataObject> entries = loadFromJson();
        DataObject newEntry = new DataObject(++currentMaxId, username, domain, email, password, description);
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

    public DataObject findById(int id) {
        List<DataObject> entries = loadFromJson();
        for (DataObject entry : entries) {
            if (entry.getId() == id) {
                return entry;
            }
        }
        return null;
    }

    public void updateEntry(int id, String username, String domain, String email, String password, String description) {
        List<DataObject> entries = loadFromJson();
        for (DataObject entry : entries) {
            if (entry.getId() == id) {
                entry.setUsername(username);
                entry.setDomain(domain);
                entry.setEmail(email);
                entry.setPassword(password);
                entry.setDescription(description);
                break;
            }
        }
        saveToFile(entries);
    }

    public void deleteEntry(int id) {
        List<DataObject> entries = loadFromJson();
        entries.removeIf(entry -> entry.getId() == id);
        saveToFile(entries);
    }
}
