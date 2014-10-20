package easyrssreader.sergeybudkov.ru.easyrssreader;

import android.os.AsyncTask;

class Load extends AsyncTask<String, Void, FeedBack> {

    @Override
    protected FeedBack doInBackground(String... strings) {
        FeedBack result = null;
        MyParser reader = new MyParser();
        try {
            result = reader.parse(strings[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}