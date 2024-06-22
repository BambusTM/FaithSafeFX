package dev.yorun.faithsafe.service;

public record Variables() {
    public static String DATA_PATH = "data.json";
    public static final String USER_PATH = "user.json";
    public static int currentUserId = 0;
    public static String currentUserPassword = "";
}
