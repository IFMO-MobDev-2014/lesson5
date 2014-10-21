package ru.ifmo.ctd.fotjev.rss_reader;

/**
 * Created by fotyev on 19-Oct-14.
 */
public interface Callback<T> {
    public void onSuccess(T obj);

    public void onError(Exception e);
}
