package you.chen.liveeventbus.eventbus;

import androidx.annotation.MainThread;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveDataEvent;
import androidx.lifecycle.LiveDataStickyEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  create by you 2019-03
 *  事件源管理
 */
final class LiveDataEventProcessor implements LiveEventProcessor {

    Map<String, LiveDataEvent> eventMap;

    Map<String, LiveDataStickyEvent> stickyEventMap;

    LiveDataEventProcessor() {
        eventMap = new ConcurrentHashMap<>();
        stickyEventMap = new ConcurrentHashMap<>();
    }

    @MainThread
    @Override
    public void removeEvent(String key) {
        eventMap.remove(key);
    }

    @MainThread
    @Override
    public void removeStickyEvent(String key) {
        stickyEventMap.remove(key);
    }

    @Override
    public <T> LiveDataEvent<T> getEvent(String key, Class<T> type) {
        LiveDataEvent liveData = eventMap.get(key);
        if (liveData == null) {
            liveData = new LiveDataEvent(key, this);
            eventMap.put(key, liveData);
        }
        return liveData;
    }

    @Override
    public <T> LiveDataEvent<T> getEvent(String key, Lifecycle.State activeLevel, Class<T> type) {
        LiveDataEvent liveData = eventMap.get(key);
        if (liveData == null) {
            liveData = new LiveDataEvent(key, this, activeLevel);
            eventMap.put(key, liveData);
        } else {
            liveData.setActiveLevel(activeLevel);
        }
        return liveData;
    }

    @Override
    public <T> LiveDataStickyEvent<T> getStickyEvent(String key, Class<T> type) {
        LiveDataStickyEvent liveData = stickyEventMap.get(key);
        if (liveData == null) {
            liveData = new LiveDataStickyEvent(key);
            stickyEventMap.put(key, liveData);
        }
        return liveData;
    }

    @Override
    public <T> LiveDataStickyEvent<T> getStickyEvent(String key, Lifecycle.State activeLevel, Class<T> type) {
        LiveDataStickyEvent liveData = stickyEventMap.get(key);
        if (liveData == null) {
            liveData = new LiveDataStickyEvent(key, activeLevel);
            stickyEventMap.put(key, liveData);
        } else {
            liveData.setActiveLevel(activeLevel);
        }
        return liveData;
    }
}
