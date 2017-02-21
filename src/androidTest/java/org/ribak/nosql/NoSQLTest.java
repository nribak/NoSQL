package org.ribak.nosql;

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
import org.ribak.nosql.utils.DbKey;

import java.util.Date;
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
        database = new KryoDatabase("test-module");
    }

    @After
    public void tearDown() throws Exception {
        database.destroy().sync();
    }

    @Test
    public void checkDb() throws Exception {
        final String group1 = "g1";
        final String group2 = "g2";
        final String[] keys = {"key0", "key1", "key2"};
        final int n = keys.length;
        for (String key : keys) {
            DbKey k1 = new DbKey.Builder().addGroup(group1).setKey(key).build();
            DbKey k2 = new DbKey.Builder().addGroup(group1).addGroup(group2).setKey(key).build();

            Person p1 = createPerson(k1.getQualifiedKey());
            Person p2 = createPerson(k2.getQualifiedKey());

            database.insert(k1, p1).sync();
            database.insert(k2, p2).sync();
        }

        int c1 = database.count(new DbKey.Builder().addGroup(group1).build()).sync();
        int c2 = database.count(new DbKey.Builder().addGroup(group1).addGroup(group2).build()).sync();

        Assert.assertEquals(n + n, c1);
        Assert.assertEquals(n, c2);

        long time = new Date().getTime();
        Map<String, ?> data = database.getAll().sync();
        Log.d("TIME", String.valueOf(new Date().getTime() - time));
        for (String key : data.keySet()) {
            Person expectedPerson = createPerson(key);
            Person person = (Person) data.get(key);

            Assert.assertNotNull(person);
            Assert.assertEquals(expectedPerson, person);
        }
    }


    private Person createPerson(String key) {
        return new Person(key, "name is: " + key);
    }
}
