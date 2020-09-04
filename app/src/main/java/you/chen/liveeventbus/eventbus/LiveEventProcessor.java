package you.chen.liveeventbus.eventbus;

import androidx.annotation.MainThread;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveDataEvent;
import androidx.lifecycle.LiveDataEventHandler;
import androidx.lifecycle.LiveDataStickyEvent;

/**
 *  create by you 2019-03
 */
public interface LiveEventProcessor extends LiveDataEventHandler {

    /**
     * 移除粘性事件
     * @param key
     */
    @MainThread
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
     * 获取普通事件
     * @param key
     * @param activeLevel
     * @param type
     * @param <T>
     * @return
     */
    <T> LiveDataEvent<T> getEvent(String key, Lifecycle.State activeLevel, Class<T> type);

    /**
     * 获取粘性事件
     * @param key
     * @param type
     * @param <T>
     * @return
     */
    <T> LiveDataStickyEvent<T> getStickyEvent(String key, Class<T> type);

    /**
     * 获取粘性事件
     * @param key
     * @param activeLevel
     * @param type
     * @param <T>
     * @return
     */
    <T> LiveDataStickyEvent<T> getStickyEvent(String key, Lifecycle.State activeLevel, Class<T> type);

}
