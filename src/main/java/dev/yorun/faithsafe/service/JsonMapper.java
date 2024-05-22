package dev.yorun.faithsafe.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.yorun.faithsafe.service.DataObject;

import java.util.ArrayList;

public class JsonMapper {

    public void saveToJson(String username, String domain, String email, String password, String description) {
        System.out.println("Saving to JSON");
        ObjectMapper objectMapper = new ObjectMapper();
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
            System.out.println("test");
            e.printStackTrace();
        }
    }
}
