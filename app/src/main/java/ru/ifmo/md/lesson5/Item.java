package ru.ifmo.md.lesson5;

public class Item {
    String title;
    String description;
    String link;
    String date;

    public Item(String[] strings) {
        title = strings[0];
        description = strings[1];
        link = strings[2];
        date = strings[3];
    }

    @Override
    public String toString() {
        return title + "\n" + date;
    }
}
