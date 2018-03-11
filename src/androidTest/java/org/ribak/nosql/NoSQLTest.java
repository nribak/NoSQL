package org.ribak.nosql;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ribak.nosql.db.DBFileStreamApi;
import org.ribak.nosql.db.NoSQLDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


/**
 * Created by nribak on 12/02/2017.
 */

@RunWith(AndroidJUnit4.class)
public class NoSQLTest {
    private NoSQLDatabase database;
    @Before
    public void setUp() throws Exception {
        DBFileStreamApi.init(InstrumentationRegistry.getTargetContext());
        database = new NoSQLDatabase("test-module-3");
    }

    @After
    public void tearDown() throws Exception {
        database.destroy().sync();
    }

    @Test
    public void db() throws Exception {
        String key = "key1";
        TestClass t1 = new TestClass(1, 2);
        database.insert(key, t1).sync();

        TestClass t2 = database.<TestClass> get(key).sync();
        Assert.assertNotNull(t2);
        Assert.assertEquals(t1, t2);
    }

    @Test
    public void db2() throws Exception {
        Bundle bundle = new Bundle();
        bundle.putInt("key1", 1);
        bundle.putInt("key2", 2);
        bundle.putString("key3", "value1");
        ArrayList<TestClass> list = new ArrayList<>();
        list.add(new TestClass(1, 2));
        list.add(new TestClass(3, 4));
        bundle.putParcelableArrayList("key4", list);
        Map<String, Object> map = new HashMap<>();
        for (String key : bundle.keySet())
            map.put(key, bundle.get(key));

        String key = "map_key";
        database.insert(key, map).sync();

        Map<String, Object> map2 = database. <Map<String, Object>> get(key).sync();
        Assert.assertNotNull(map2);
        Assert.assertEquals(map, map2);
    }


    @Test
    public void db3() throws Exception {
        Bundle bundle = new Bundle();
        bundle.putInt("key1", 1);
        bundle.putInt("key2", 2);
        bundle.putString("key3", "value1");
        ArrayList<TestClass> list = new ArrayList<>();
        list.add(new TestClass(1, 2));
        list.add(new TestClass(3, 4));
        bundle.putParcelableArrayList("key4", list);
        bundle.putIntArray("key5", new int[] {10, 20, 30});
        String key = "bundle_key";
        long time = System.currentTimeMillis();
        database.insert(key, bundle).sync();
        Log.d("TIME1", String.valueOf(System.currentTimeMillis() - time));
        time = System.currentTimeMillis();
        Bundle bundle2 = database.<Bundle> get(key).sync();
        Log.d("TIME2", String.valueOf(System.currentTimeMillis() - time));
        Assert.assertNotNull(bundle2);
        boolean eq = checkBundles(bundle, bundle2);
        Assert.assertTrue(eq);
    }

    private boolean checkBundles(Bundle bundle1, Bundle bundle2) {
        Set<String> set1 = bundle1.keySet();
        Set<String> set2 = bundle2.keySet();
        if(set1.size() != set2.size())
            return false;
        for (String key : set1) {
            Object val1 = bundle1.get(key);
            Object val2 = bundle2.get(key);
            if(!testEquality(val1, val2))
                return false;
        }
        return true;
    }

    private static boolean testEquality(Object o1, Object o2) {
       if(o1 == null || o2 == null)
           return o1 == o2;

       if(o1.getClass().isArray() && o2.getClass().isArray())
           return Array.getLength(o1) == Array.getLength(o2);
       return Objects.equals(o1, o2);
    }

    @Test
    public void db4() throws Exception {
//        for (int i = 0; i < 100; i++)
//            database.insert("key" + i, "string" + i).async();
        database.insert("key-sync", "string-sync").sync();
    }

    private static class TestClass implements Parcelable {
        private int x, y;

        private TestClass() { }

        private TestClass(int x, int y) {
            this.x = x;
            this.y = y;
        }

        protected TestClass(Parcel parcel) {
            this.x = parcel.readInt();
            this.y = parcel.readInt();
        }

        @Override
        public boolean equals(Object obj) {
            TestClass rhs = (TestClass) obj;
            return x == rhs.x && y == rhs.y;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(x);
            dest.writeInt(y);
        }
    }
}
