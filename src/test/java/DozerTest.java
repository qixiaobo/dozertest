import dest.Super2Bean;
import org.dozer.DozerBeanMapper;
import org.dozer.cache.CacheManager;
import org.dozer.cache.DozerCacheType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import src.ChildBean;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by qixiaobo on 2017/6/27.
 */
public class DozerTest {
    private static DozerBeanMapper mapper;

    @BeforeClass
    public static void prepareDate() {
        mapper = new DozerBeanMapper(Arrays.asList("dozer.xml"));
    }

    @Before
    public void clearSuperCache() {
        try {
            Field cacheManager = DozerBeanMapper.class.getDeclaredField("cacheManager");
            cacheManager.setAccessible(true);
            ((CacheManager) cacheManager.get(mapper)).getCache(DozerCacheType.SUPER_TYPE_CHECK.name()).clear();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Before
    public void clearCLassMapping() {
        try {
            Field customMappings = DozerBeanMapper.class.getDeclaredField("customMappings");
            customMappings.setAccessible(true);
            customMappings.set(mapper, null);
            //for dozer 5.6.0
            Field init = DozerBeanMapper.class.getDeclaredField("initializing");
            init.setAccessible(true);
            ((AtomicBoolean)init.get(mapper)).set(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSuperFirst() {
        ChildBean childBean = new ChildBean();
        childBean.setSup2("123");
        Super2Bean map = mapper.map(childBean, Super2Bean.class);
        dest.ChildBean map1 = mapper.map(childBean, dest.ChildBean.class);
        Assert.assertEquals("", map.getSup1(), map1.getSup1());
    }

    @Test
    public void testChildFirst() {
        ChildBean childBean = new ChildBean();
        childBean.setSup2("123");
        dest.ChildBean map1 = mapper.map(childBean, dest.ChildBean.class);
        Super2Bean map = mapper.map(childBean, Super2Bean.class);
        Assert.assertEquals("", map.getSup1(), map1.getSup1());
    }

}