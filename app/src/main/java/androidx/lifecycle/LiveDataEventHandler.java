package androidx.lifecycle;

import androidx.annotation.NonNull;

public interface LiveDataEventHandler {

    /**
     * 接收事件时的可见级别
     * @return
     */
    @NonNull
    Lifecycle.State activeLevel();

    /**
     * 移除消息事件
     * @param key
     */
    void removeEvent(String key);

    /**
     * 移除粘性事件
     * @param key
     */
    void removeStickyEvent(String key);

    /**
     * 获取普通事件
     * @param key
     * @param type
     * @param <T>
     * @return
     */
    <T> LiveDataEvent<T> getEvent(String key, Class<T> type);

    /**
     * 获取粘性事件
     * @param key
     * @param type
     * @param <T>
     * @return
     */
    <T> LiveDataStickyEvent<T> getStickyEvent(String key, Class<T> type);
}
