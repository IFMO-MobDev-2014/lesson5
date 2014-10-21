package ru.ifmo.md.lesson5;

/**
 * Created by Svet on 20.10.2014.
 */
public class Message {
    String title;
    String description;
    String link;
    String source;

    Message() {
        title = "";
        description = "";
        link = "";
        source = "";
    }

    Message(String t, String m, String l, String s) {
        title = t;
        description = m;
        link = l;
        source = s;
    }
}
