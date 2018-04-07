package org.ribak.nosql;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ribak.nosql.db.DBFileStreamApi;
import org.ribak.nosql.db.NoSQLDatabase;


/**
 * Created by nribak on 12/02/2017.
 */

@RunWith(AndroidJUnit4.class)
public class NoSQLTest {
    private NoSQLDatabase database;
    private Bitmap bitmap;
    @Before
    public void setUp() throws Exception {
        DBFileStreamApi.init(InstrumentationRegistry.getTargetContext());
        database = new NoSQLDatabase("test-module-4");
        createBitmap();
    }

    @After
    public void tearDown() throws Exception {
        database.destroy().sync();
    }

    private void createBitmap() {
        int size = 100;
        bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        for (int x = 0; x < size; x++)
            for (int y = 0; y < size; y++)
                bitmap.setPixel(x, y, (x < size / 2) ? Color.RED : Color.TRANSPARENT);
    }

    @Test
    public void runDB() throws Exception {
        final String key = "key1";
        Person p = new Person(String.valueOf(1), "nadav");
        Boolean success = database.insert(key, p).sync();
        Assert.assertNotNull(success);
        Assert.assertTrue(success);

        Person p2 = database.<Person> get(key).sync();
        Assert.assertNotNull(p2);
        Assert.assertEquals(p.getId(), p2.getId());
    }

    @Test
    public void simpleDB() throws Exception {
        String[] keys = {"k1", "k2", "k3"};
        String[] values = {"v1", "v2", "v3"};

        int n = keys.length;
        for (int i = 0; i < n; i++)
            database.insert(keys[i], values[i]).sync();

        for (int i = 0; i < n; i++) {
            String v = database.<String> get(keys[i]).sync();
            Assert.assertNotNull(v);
            Assert.assertEquals(values[i], v);
        }
    }

    @Test
    public void bitmaps1() throws Exception {
        final String key = "bitmap-key";
        database.insert(key, bitmap).sync();

        Bitmap b2 = database.<Bitmap> get(key).sync();

        Assert.assertNotNull(b2);
        Assert.assertTrue(b2.sameAs(bitmap));
    }
}
