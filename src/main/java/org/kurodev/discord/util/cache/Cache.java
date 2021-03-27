package org.kurodev.discord.util.cache;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author kuro
 **/
public class Cache<T> {
    private final int maxAge;
    private final ChronoUnit unit;
    private T cached = null;
    private LocalDateTime current;
    private Supplier<T> supplier;
    private Consumer<T> onUpdate;

    public Cache(int maxAge, ChronoUnit unit) {
        this.maxAge = maxAge;
        this.unit = unit;
    }

    public Cache(int maxAge, TimeUnit unit) {
        this(maxAge, unit.toChronoUnit());
    }

    public Cache() {
        this(3, TimeUnit.MINUTES);
    }

    public Cache(T initial, int maxAge, TimeUnit unit) {
        this(maxAge, unit);
        update(initial);
    }

    public boolean isDirty() {
        return current == null || current.plus(maxAge, unit).isBefore(LocalDateTime.now());
    }

    public T getCachedItem() {
        return getCachedItem(null);
    }

    public T getCachedItem(T ifDirty) {
        boolean dirty = isDirty();
        if (supplier != null && dirty) {
            update(supplier.get());
            return cached;
        }
        if (dirty) {
            return ifDirty;
        }
        return cached;
    }

    public void update(T cached) {
        this.cached = cached;
        current = LocalDateTime.now();
        if (onUpdate != null) {
            onUpdate.accept(cached);
        }
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

    public void setOnUpdate(Consumer<T> onUpdate) {
        this.onUpdate = onUpdate;
    }
}
