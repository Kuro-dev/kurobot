package org.kurodev.util.cache;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author kuro
 **/
public class SelfUpdatingCache<T> extends Cache<T> {
    private final Supplier<T> updater;

    public SelfUpdatingCache(int maxAge, TimeUnit unit, Supplier<T> updater) {
        super(maxAge, unit);
        this.updater = updater;
    }

    @Override
    public T getCachedItem() {
        if (isDirty()) {
            update(updater.get());
        }
        return super.getCachedItem();
    }
}
