package you.chen.liveeventbus.eventbus;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveDataEvent;
import androidx.lifecycle.LiveDataStickyEvent;


/**
 *  create by you 2019-03
 */
public final class LiveEventBus {

    LiveEventProcessor eventProcessor;

    private LiveEventBus() {
        eventProcessor = new LiveDataEventProcessor();
    }

    private interface LiveEventBusHolder {
        LiveEventBus INSTANCE = new LiveEventBus();
    }

    public static void removeEvent(String key) {
        LiveEventBusHolder.INSTANCE.eventProcessor.removeEvent(key);
    }

    public static void removeStickyEvent(String key) {
        LiveEventBusHolder.INSTANCE.eventProcessor.removeStickyEvent(key);
    }

    public static <T> LiveDataEvent<T> with(String key, Class<T> type) {
        return LiveEventBusHolder.INSTANCE.eventProcessor.getEvent(key, type);
    }

    public static <T> LiveDataEvent<T> with(String key, Lifecycle.State activeLevel, Class<T> type) {
        return LiveEventBusHolder.INSTANCE.eventProcessor.getEvent(key, activeLevel, type);
    }

    public static <T> LiveDataStickyEvent<T> withSticky(String key, Class<T> type) {
        return LiveEventBusHolder.INSTANCE.eventProcessor.getStickyEvent(key, type);
    }

    public static <T> LiveDataStickyEvent<T> withSticky(String key, Lifecycle.State activeLevel, Class<T> type) {
        return LiveEventBusHolder.INSTANCE.eventProcessor.getStickyEvent(key, activeLevel, type);
    }
}
