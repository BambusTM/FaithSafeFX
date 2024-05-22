package dev.yorun.faithsafe.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

public class JsonMapper {
    public void saveToJson(String username, String domain, String email, String password, String description) {
        System.out.println("Saving to JSON");
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
            String jsonData = objectMapper.writeValueAsString(dataObjectList);
            System.out.println(jsonData);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
