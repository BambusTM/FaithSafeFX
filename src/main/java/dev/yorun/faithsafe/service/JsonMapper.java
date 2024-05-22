package dev.yorun.faithsafe.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;

import java.io.IOException;
import java.util.ArrayList;

public class JsonMapper {
    public void saveToJson(String username, String domain, String email, String password, String description) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        ArrayList<DataObject> dataObjectList = new ArrayList<>();
        DataObject d1 = new DataObject(
                username,
                domain,
                email,
                password,
                description
        );
        dataObjectList.add(d1);
        try {
            objectMapper.writeValue(new File("\\FaithSafeFX\\data.json"), dataObjectList);
        } catch (IOException e) {
            System.err.println("Failed to save data: " + e.getMessage());
        }
    }
}
