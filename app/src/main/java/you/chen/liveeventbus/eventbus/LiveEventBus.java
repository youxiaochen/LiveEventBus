package you.chen.liveeventbus.eventbus;

import androidx.lifecycle.LiveDataEvent;
import androidx.lifecycle.LiveDataEventHandler;
import androidx.lifecycle.LiveDataStickyEvent;

public final class LiveEventBus {

    private LiveDataEventHandler eventHandler;

    private LiveEventBus() {
        eventHandler = new LiveEventHandler();
    }

    private interface EventBusHolder {
        LiveEventBus INSTANCE = new LiveEventBus();
    }

    /**
     * 获取普通的事件
     * @param key
     * @param type
     * @param <T>
     * @return
     */
    public static  <T> LiveDataEvent<T> with(String key, Class<T> type) {
        return EventBusHolder.INSTANCE.eventHandler.getEvent(key, type);
    }

    /**
     * 获取粘性事件
     * @param key
     * @param type
     * @param <T>
     * @return
     */
    public static  <T> LiveDataStickyEvent<T> withSticky(String key, Class<T> type) {
        return EventBusHolder.INSTANCE.eventHandler.getStickyEvent(key, type);
    }

    /**
     * 移除粘性事件, 粘性事件需要手动移除
     * @param key
     */
    public static void removeStickyEvent(String key) {
        EventBusHolder.INSTANCE.eventHandler.removeStickyEvent(key);
    }

}
