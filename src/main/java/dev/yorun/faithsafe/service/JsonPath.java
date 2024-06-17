package dev.yorun.faithsafe.service;

import com.fasterxml.jackson.core.type.TypeReference;
import dev.yorun.faithsafe.objects.DataObject;
import dev.yorun.faithsafe.objects.UserObject;

import java.util.List;

import static dev.yorun.faithsafe.service.Variables.DATA_PATH;
import static dev.yorun.faithsafe.service.Variables.USER_PATH;

public enum JsonPath {
    User(false, USER_PATH, UserObject.class, new TypeReference<List<UserObject>>() {
    }),
    Data(true, DATA_PATH, DataObject.class, new TypeReference<List<DataObject>>() {
    });

    boolean encrypted;
    String path;
    Class<?> clazz;
    TypeReference<?> typeReference;
    JsonPath(boolean encrypted, String path, Class<?> clazz, TypeReference<?> typeReference) {
        this.encrypted = encrypted;
        this.path = path;
        this.clazz = clazz;
        this.typeReference = typeReference;
    }

    public void updatePath(String path) {
        this.path = path;
    }

    <T> Class<T> getClazz() {
        return (Class<T>) this.clazz;
    }
}
