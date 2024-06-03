package dev.yorun.faithsafe.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.yorun.faithsafe.objects.DataObject;
import dev.yorun.faithsafe.objects.UserObject;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

@Deprecated()
public class JsonMapperOld {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String path;
    private int currentMaxId = 0;
    private int userId = 0;

    public JsonMapperOld(String inputPath) {
        this.path = inputPath;
        List<DataObject> entries = loadFromJson();
        if (!entries.isEmpty()) {
            currentMaxId = entries.stream().mapToInt(DataObject::getId).max().orElse(0);
        }
        List<UserObject> userEntries = loadUserFromJson();
        if (!userEntries.isEmpty()) {
            userId = userEntries.stream().mapToInt(UserObject::getId).max().orElse(0);
        }

    }

    public void saveToJson(String username, String domain, String email, String password, String description) {
        List<DataObject> entries = loadFromJson();
        DataObject newEntry = new DataObject(++currentMaxId, Variables.currentUserId, username, domain, email, password, description);
        entries.add(newEntry);
        saveToFile(entries);
    }

    public void saveUserToJson(String username, String password) {
        List<UserObject> entries = loadUserFromJson();
        // DO NOT USE THIS FUNCTION ANYMORE
        UserObject newUser = new UserObject(++userId, username, password);
        entries.add(newUser);
        saveToFile(entries);
    }

    public List<DataObject> loadFromJson() {
        try {
            File file = new File(path);
            if (file.exists()) {
                return objectMapper.readValue(file, new TypeReference<>() {
                });
            }
        } catch (IOException e) {
            System.err.println("Failed to load existing data: " + e.getMessage());
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<UserObject> loadUserFromJson() {
        try {
            File file = new File(path);
            if (file.exists()) {
                return objectMapper.readValue(file, new TypeReference<>() {
                });
            }
        } catch (IOException e) {
            System.err.println("Failed to load existing data: " + e.getMessage());
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private void saveToFile(List<?> entries) {
        try {
            var stringWriter = new StringWriter();
            objectMapper.writeValue(stringWriter, entries);
            objectMapper.writeValue(new File(path), entries);

            String input = stringWriter.toString();
            System.out.println(input);

            stringWriter.close();
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

    public UserObject findUserByUsername(String username) {
        List<UserObject> entries = loadUserFromJson();
        for (UserObject entry : entries) {
            if (entry.getUsername().equals(username)) {
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
