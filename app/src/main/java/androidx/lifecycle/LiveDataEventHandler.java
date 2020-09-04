package androidx.lifecycle;

import androidx.annotation.MainThread;

/**
 *  create by you 2019-03
 *  事件管理
 */
public interface LiveDataEventHandler {

    /**
     * 移除消息事件
     * @param key
     */
    @MainThread
    void removeEvent(String key);
}
