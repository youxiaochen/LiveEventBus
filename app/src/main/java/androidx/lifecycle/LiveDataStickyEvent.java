package androidx.lifecycle;

import androidx.annotation.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static androidx.lifecycle.Lifecycle.State.DESTROYED;

public class LiveDataStickyEvent<T> extends LiveData<T> {

    final String key;

    final LiveDataEventHandler eventHandler;

    public LiveDataStickyEvent(String key, LiveDataEventHandler eventHandler) {
        this.key = key;
        this.eventHandler = eventHandler;
    }

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        if (owner.getLifecycle().getCurrentState() == DESTROYED) {
            return;
        }
        try {
            LifecycleBoundObserver wrapper = new EventLifecycleBoundObserver(owner, observer, eventHandler.activeLevel());
            LifecycleBoundObserver existing = (LifecycleBoundObserver) observersMapPut(observer, wrapper);
            if (existing != null && !existing.isAttachedTo(owner)) {
                throw new IllegalArgumentException("Cannot add the same observer" + " with different lifecycles");
            }
            if (existing != null) {
                return;
            }
            owner.getLifecycle().addObserver(wrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void postStickyValue(T value) {
        super.postValue(value);
    }

    public void setStickyValue(T value) {
        super.setValue(value);
    }

    /**
     * google工程师封装的太严实
     * @param observer
     * @param wrapper
     * @throws Exception
     */
    private Object observersMapPut(Object observer, Object wrapper) throws Exception {
        Field fieldObservers = LiveData.class.getDeclaredField("mObservers");
        fieldObservers.setAccessible(true);
        Object mObservers = fieldObservers.get(this);
        Class<?> classOfSafeIterableMap = mObservers.getClass();
        Method putIfAbsent = classOfSafeIterableMap.getDeclaredMethod("putIfAbsent", Object.class, Object.class);
        putIfAbsent.setAccessible(true);
        return putIfAbsent.invoke(mObservers, observer, wrapper);
    }

    private class EventLifecycleBoundObserver extends LifecycleBoundObserver {

        Lifecycle.State activeLevel;

        EventLifecycleBoundObserver(@NonNull LifecycleOwner owner, Observer<? super T> observer, Lifecycle.State activeLevel) {
            super(owner, observer);
            this.activeLevel = activeLevel;
        }

        @Override
        boolean shouldBeActive() {
            return mOwner.getLifecycle().getCurrentState().isAtLeast(activeLevel);
        }

    }

}
