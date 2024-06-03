package dev.yorun.faithsafe.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonMapper<T extends BasicJson> {
    public JsonPath path;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private int currentId = 0;
    private Class<T> clazz;

    public JsonMapper(JsonPath path) {
        this.path = path;
        this.clazz = path.getClazz();

        List<T> entries = loadFromJson();

        if (!entries.isEmpty()) {
            currentId = entries.stream().mapToInt(T::getId).max().orElse(0);
        }
    }

    public void saveToJson(T object) {
        List<T> entries = loadFromJson();
        object.setId(++currentId);
        entries.add(object);
        saveToFile(entries);
    }

//    public void saveToJson(Object... objects) {
//        List<T> entries = loadFromJson();
//        List<Class<?>> list = new ArrayList<>();
//        list.add(Integer.class);
//        for (Object object : objects) {
//            Class<?> aClass = object.getClass();
//            list.add(aClass);
//        }
//        Class<?>[] classes = new Class[list.size()];
//        classes = list.toArray(classes);
//
//        try {
//            List<Object> idWithVarargs = new ArrayList<>();
//            idWithVarargs.add(++currentId);
//            idWithVarargs.add(objects);
//            T newT = clazz.getDeclaredConstructor(classes).newInstance(idWithVarargs);
//            newT.setId(currentId);
//            entries.add(newT);
//            saveToFile(entries);
//        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
//            System.err.println("Could not get constructor");
//            e.printStackTrace();
//        }
//    }

    public List<T> loadFromJson() {
        try {
            File file = new File(path.path);

            if (file.exists()) {
                return (List<T>) objectMapper.readValue(file, this.path.typeReference);
            }
        } catch (IOException e) {
            System.err.println("Could not load data");
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    private void saveToFile(List<T> entries) {
        try {
            objectMapper.writeValue(new File(path.path), entries);
        } catch (IOException e) {
            System.err.println("Could not load data");
            e.printStackTrace();
        }
    }

    public T findById(int id) {
        List<T> entries = loadFromJson();
        for (T entry : entries) {
            if (entry.getId() == id) {
                return entry;
            }
        }
        return null;
    }

    public void updateEntry(int id, T object) {
        List<T> entries = loadFromJson();
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getId() == id) {
                entries.set(i, object);
            }
        }

        saveToFile(entries);
    }

    public void deleteEntry(int id) {
        List<T> entries = loadFromJson();

        entries.removeIf(entry -> entry.getId() == id);
        saveToFile(entries);
    }
}
