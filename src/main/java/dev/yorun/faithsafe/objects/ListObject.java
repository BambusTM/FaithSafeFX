package dev.yorun.faithsafe.objects;

public class ListObject {
    private int id;
    private String content;

    public ListObject(int id, String content) {
        this.id = id;
        this.content = content;
    }

    @Override
    public String toString() {
        return content;
    }

    public int getId() {
        return id;
    }
}
