package discord;

import org.junit.Assert;
import org.junit.Test;
import org.kurodev.discord.util.cache.Cache;
import org.kurodev.discord.util.cache.SelfUpdatingCache;

import java.util.concurrent.TimeUnit;

/**
 * @author kuro
 **/
public class CacheTest {
    @Test
    public void testCacheIsNotTooOld() throws InterruptedException {
        Cache<String> cache = new Cache<>(10, TimeUnit.MILLISECONDS);
        cache.update("test");
        Thread.sleep(30);
        Assert.assertTrue(cache.isDirty());
        cache.update("test2");
        Assert.assertFalse(cache.isDirty());
    }

    @Test
    public void selfRefreshingCacheShouldUpdateItself() throws InterruptedException {
        Cache<String> cache = new SelfUpdatingCache<>(10, TimeUnit.MILLISECONDS, () -> "test2");
        cache.update("test");
        Assert.assertEquals("test", cache.getCachedItem());
        Thread.sleep(30);
        Assert.assertEquals("test2", cache.getCachedItem());
    }
}
