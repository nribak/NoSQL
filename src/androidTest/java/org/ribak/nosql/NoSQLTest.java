package org.ribak.nosql;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ribak.nosql.db.KDB;
import org.ribak.nosql.db.KryoDatabase;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by nribak on 12/02/2017.
 */

@RunWith(AndroidJUnit4.class)
public class NoSQLTest {
    private KryoDatabase database;
    @Before
    public void setUp() throws Exception {
        KDB.init(InstrumentationRegistry.getTargetContext());
        database = new KryoDatabase("test-module-3");
    }

    @After
    public void tearDown() throws Exception {
        database.destroy().sync();
    }
    @Test
    public void checkDb() throws Exception {
        final String[] keys = {"key0", "key1", "key2", "specialKey1", "specialKey2"};
        final int n = 3;
        for (String key : keys) {
            Person person = createPerson(key);
            database.insert(key, person).sync();
        }

        int count = database.count("key").sync();
        Assert.assertEquals(n, count);

        Map<String, ?> data = database.getAll().sync();
        Assert.assertNotNull(data);
        Assert.assertEquals(keys.length, data.size());
        for (String key : data.keySet()) {
            Person expected = createPerson(key);
            Person person = (Person) data.get(key);

            Assert.assertNotNull(person);
            Assert.assertEquals(expected, person);
        }
    }


    private Person createPerson(String key) {
        return new Person(key, "name is: " + key);
    }


    @Test
    public void timeSingle() throws Exception {
        final int N = 10000;
        long time = new Date().getTime();
        for (int i = 0; i < N; i++) {
            Person person = createPerson("key" + i);
            database.insert(String.valueOf(i), person).sync();
        }
        Log.d("TIME1", String.valueOf(new Date().getTime() - time));
        time = new Date().getTime();
        for (int i = 0; i < N; i++) {
            Person expected = createPerson("key" + i);
            Person person = database.<Person> get(String.valueOf(i)).sync();

            Assert.assertNotNull(person);
            Assert.assertEquals(expected, person);
        }
        Log.d("TIME2", String.valueOf(new Date().getTime() - time));
    }

    @Test
    public void timeMultiple() throws Exception {
        final int N = 10000;
        Person[] persons = new Person[N];
        for (int i = 0; i < N; i++)
            persons[i] = createPerson("key" + i);

        final String key = "key-multiple";
//        final DbKey dbKey = new DbKey.Builder().setKey("keyBatch").build();
        long time = new Date().getTime();
        database.insertArray(key, persons).sync();
        Log.d("TIME1", String.valueOf(new Date().getTime() - time));

        time = new Date().getTime();
        List<Person> personsList = database.<Person> getArray(key).sync();
        Log.d("TIME2", String.valueOf(new Date().getTime() - time));

        for (int i = 0; i < N; i++) {
            Person expected = persons[i];
            Person person = personsList.get(i);
            Assert.assertNotNull(person);
            Assert.assertEquals(expected, person);
        }
    }

    @Test
    public void multipleDBs() throws Exception {
        KryoDatabase secondDatabase = new KryoDatabase("test-2-module-4");
        final String[] keys = {"key0", "key1", "key2", "specialKey1", "specialKey2"};
        Map<String, Object> expected = new HashMap<>();

        for (String key : keys) {
            Person person = createPerson(key);
            database.insert(key, person).sync();
            secondDatabase.insert(key, person).sync();
            expected.put(key, person);
        }

        Map<String, ?> map1 = database.getAll().sync();
        Map<String, ?> map2 = secondDatabase.getAll().sync();

        Assert.assertNotNull(map1);
        Assert.assertNotNull(map2);

        Assert.assertEquals(expected, map1);
        Assert.assertEquals(expected, map2);

        secondDatabase.destroy().sync();
    }

    @Test
    public void bitmaps() throws Exception {
        final String key = "bitmap_key";
        Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565);
        bitmap.eraseColor(Color.RED);

        database.insert(key, bitmap).sync();
        Bitmap b = database.<Bitmap> get(key).sync();

        Assert.assertNotNull(b);

        boolean same = bitmap.sameAs(b);
        Assert.assertTrue(same);

        database.delete(key).sync();
        b = database.<Bitmap> get(key).sync();
        Assert.assertNull(b);

    }
}
