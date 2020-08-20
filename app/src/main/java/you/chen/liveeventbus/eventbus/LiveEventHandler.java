package you.chen.liveeventbus.eventbus;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveDataEvent;
import androidx.lifecycle.LiveDataEventHandler;
import androidx.lifecycle.LiveDataStickyEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final class LiveEventHandler implements LiveDataEventHandler {

    Map<String, LiveDataEvent> eventMap;

    Map<String, LiveDataStickyEvent> stickyEventMap;

    LiveEventHandler() {
        eventMap = new ConcurrentHashMap<>();
        stickyEventMap = new ConcurrentHashMap<>();
    }

    @NonNull
    @Override
    public Lifecycle.State activeLevel() {
        return Lifecycle.State.CREATED;
    }

    @Override
    public void removeEvent(String key) {
        eventMap.remove(key);
    }

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
    public <T> LiveDataStickyEvent<T> getStickyEvent(String key, Class<T> type) {
        LiveDataStickyEvent liveData = stickyEventMap.get(key);
        if (liveData == null) {
            liveData = new LiveDataStickyEvent(key, this);
            stickyEventMap.put(key, liveData);
        }
        return liveData;
    }

}
