package androidx.lifecycle;

import androidx.annotation.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 *  create by you 2019-03
 *  粘性事件
 * @param <T>
 *
 */
public class LiveDataStickyEvent<T> extends LiveData<T> {
    /**
     * 接收事件的级别, 默认为CREATED, 也可以设置 STARTED, RESUMED
     */
    @NonNull
    Lifecycle.State activeLevel;

    final String key;

    public LiveDataStickyEvent(String key) {
        this(key, Lifecycle.State.CREATED);
    }

    public LiveDataStickyEvent(String key, @NonNull Lifecycle.State activeLevel) {
        this.key = key;
        this.activeLevel = activeLevel;
    }

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        if (owner.getLifecycle().getCurrentState() == Lifecycle.State.DESTROYED) {
            return;
        }
        try {
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

    public void postStickyValue(T value) {
        super.postValue(value);
    }

    public void setStickyValue(T value) {
        super.setValue(value);
    }

    public final void setActiveLevel(@NonNull Lifecycle.State activeLevel) {
        if (this.activeLevel == activeLevel) return;
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

}
