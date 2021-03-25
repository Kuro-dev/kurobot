package org.kurodev.discord.util.cache;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author kuro
 **/
public class Cache<T> {
    private final int maxAge;
    private final TimeUnit unit;
    private T cached = null;
    private LocalDateTime current;
    private Supplier<T> supplier;

    public Cache(int maxAge, TimeUnit unit) {
        this.maxAge = maxAge;
        this.unit = unit;
    }

    public Cache(T initial, int maxAge, TimeUnit unit) {
        this(maxAge, unit);
        cached = initial;
        current = LocalDateTime.now();
    }

    public Cache(int maxAge, TimeUnit unit, Supplier<T> supplier) {
        this(maxAge, unit);
        setOnDirty(supplier);
    }

    public boolean isDirty() {
        return current == null || current.plus(maxAge, unit.toChronoUnit()).isBefore(LocalDateTime.now());
    }

    public T getCachedItem() {
        return getCachedItem(null);
    }

    public T getCachedItem(T ifNull) {
        if (supplier != null && isDirty()) {
            update(supplier.get());
        }
        if (cached == null) {
            return ifNull;
        }
        return cached;
    }

    public void update(T cached) {
        this.cached = cached;
        current = LocalDateTime.now();
    }

    /**
     * @param supplier A function to automatically update the cache using the given function
     */
    public void setOnDirty(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    /**
     * Forces the cache to update regardless of state (dirty or not).
     *
     * @throws NullPointerException if no supplier was given.
     * @apiNote This method will always fail (throw an exception) unless {@link #setOnDirty(Supplier)} has been invoked
     * previously with a parameter that is not {@code null}.
     * @see #setOnDirty(Supplier)
     */
    public void forceUpdate() {
        Objects.requireNonNull(supplier);
        update(supplier.get());
    }
}
