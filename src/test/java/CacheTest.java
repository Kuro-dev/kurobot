import org.junit.Assert;
import org.junit.Test;
import org.kurodev.util.cache.Cache;

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
}
