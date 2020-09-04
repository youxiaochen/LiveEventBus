package androidx.lifecycle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.internal.SafeIterableMap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static androidx.lifecycle.Lifecycle.State.DESTROYED;

/**
 *  create by you 2019-03
 *  普通事件
 * @param <T>
 */
public class LiveDataEvent<T> extends LiveData<T> {
    /**
     * 接收事件的级别, 默认为CREATED, 也可以设置 STARTED, RESUMED
     */
    @NonNull
    Lifecycle.State activeLevel;

    final String key;

    final LiveDataEventHandler eventHandler;

    final SafeIterableMap<Observer, ObserverWrapper<? super T>> everObserverMap = new SafeIterableMap<>();

    public LiveDataEvent(String key, LiveDataEventHandler eventHandler) {
        this(key, eventHandler, Lifecycle.State.CREATED);
    }

    public LiveDataEvent(String key, LiveDataEventHandler eventHandler, @NonNull Lifecycle.State activeLevel) {
        this.key = key;
        this.eventHandler = eventHandler;
        this.activeLevel = activeLevel;
    }

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> o) {
        if (owner.getLifecycle().getCurrentState() == DESTROYED) {
            return;
        }
        try {
            ObserverWrapper<? super T> observer = new ObserverWrapper<>(o, getVersion() > START_VERSION);
            LifecycleBoundObserver wrapper = new LifecycleBoundObserver(owner, observer) {
                @Override
                boolean shouldBeActive() {
                    return mOwner.getLifecycle().getCurrentState().isAtLeast(activeLevel);
                }
            };
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

    @Override
    public void observeForever(@NonNull Observer<? super T> o) {
        ObserverWrapper<? super T> observer = new ObserverWrapper<>(o, getVersion() > START_VERSION);
        everObserverMap.putIfAbsent(o, observer);
        super.observeForever(observer);
    }

    @Override
    public void removeObserver(@NonNull Observer<? super T> observer) {
        super.removeObserver(observer);
        everObserverMap.remove(observer);
        if (!hasObservers()) {
            eventHandler.removeEvent(this.key);
        }
    }

    @Override
    public void postValue(T value) {
        super.postValue(value);
    }

    @Override
    public void setValue(T value) {
        super.setValue(value);
    }

    public final void setActiveLevel(@NonNull Lifecycle.State activeLevel) {
        this.activeLevel = activeLevel;
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

    private class ObserverWrapper<T> implements Observer<T> {
        @NonNull
        final Observer<T> observer;
        //非粘性事件时,提前发送的事件
        boolean prePostEvent;

        ObserverWrapper(@NonNull Observer<T> observer, boolean prePostEvent) {
            this.observer = observer;
            this.prePostEvent = prePostEvent;
        }

        @Override
        public void onChanged(@Nullable T t) {
            if (prePostEvent) {
                prePostEvent = false;
                return;
            }
            try {
                observer.onChanged(t);
            } catch (Exception e) {
                //Log.i("you", e.toString());
            }
        }
    }

}
