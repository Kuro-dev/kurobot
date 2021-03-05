package org.kurodev.discord.util.cache;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author kuro
 **/
public class Cache<T> {
    private final int maxAge;
    private final TimeUnit unit;
    private T cached = null;
    private LocalDateTime current;

    public Cache(int maxAge, TimeUnit unit) {
        this.maxAge = maxAge;
        this.unit = unit;
    }

    public Cache(T item, int maxAge, TimeUnit unit) {
        this(maxAge, unit);
        cached = item;
        current = LocalDateTime.now();
    }

    public boolean isDirty() {
        return current == null || current.plus(maxAge, unit.toChronoUnit()).isBefore(LocalDateTime.now());
    }

    public T getCachedItem() {
        return cached;
    }

    public void update(T cached) {
        this.cached = cached;
        current = LocalDateTime.now();
    }
}
