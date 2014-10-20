package easyrssreader.sergeybudkov.ru.easyrssreader;

import java.util.ArrayList;

public class FeedBack {
    private Exception exception;
    private ArrayList<Entry> array;

    public FeedBack(ArrayList<Entry> array, Exception exception) {
        this.exception = exception;
        this.array = array;
    }

    public Exception getException() {
        return exception;
    }

    public ArrayList<Entry> getArray() {
        return array;
    }

}
