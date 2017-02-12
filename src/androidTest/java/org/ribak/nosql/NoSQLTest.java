package org.ribak.nosql;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ribak.nosql.db.ModulesManager;
import org.ribak.nosql.db.SnappyDatabase;
import org.ribak.nosql.utils.DbKey;

import java.util.Map;

/**
 * Created by nribak on 12/02/2017.
 */

@RunWith(AndroidJUnit4.class)
public class NoSQLTest {
    private SnappyDatabase database;
    @Before
    public void setUp() throws Exception {
        SnappyDatabase.init(InstrumentationRegistry.getTargetContext());
        database = (SnappyDatabase) ModulesManager.getInstance().getDB("tests-2");
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

        DbKey.Builder builder1 = new DbKey.Builder().addGroup(group1);
        for (String k : keys) {
            DbKey key = builder1.setKey(k).build();
            database.insert(key, new Person(k, "name is: " + k)).sync();
        }

        DbKey.Builder builder2 = new DbKey.Builder().addGroup(group1).addGroup(group2);
        for (String k : keys) {
            DbKey key = builder2.setKey(k).build();
            database.insert(key, new Person(k, "name is: " + k)).sync();
        }

        int n = keys.length;
        int count = database.count(builder1.build()).sync();
        Assert.assertEquals(n + n, count);

        count = database.countAll().sync();
        Assert.assertEquals(n + n, count);

        count = database.count(builder2.build()).sync();
        Assert.assertEquals(n, count);


        for (String k : keys) {
            DbKey dbKey1 = builder1.setKey(k).build();
            DbKey dbKey2 = builder2.setKey(k).build();

            Person p1 = database.<Person> get(dbKey1, null).sync();
            Person p2 = database.<Person> get(dbKey2, null).sync();

            Assert.assertNotNull(p1);
            Assert.assertNotNull(p2);

            Person p = createPerson(k);
            Assert.assertEquals(p, p1);
            Assert.assertEquals(p, p2);
        }

        Map<DbKey, ?> data = database.getAll((String) null).sync();
        for (DbKey dbKey : data.keySet()) {
            Person p = createPerson(dbKey.getKey());
            Person person = (Person) data.get(dbKey);
            Assert.assertNotNull(person);
            Assert.assertEquals(p, person);
        }
    }


    private Person createPerson(String key) {
        return new Person(key, "name is: " + key);
    }
}
