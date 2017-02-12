package org.ribak.nosql.db;

import android.content.Context;
import android.support.annotation.NonNull;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import org.ribak.nosql.IDatabaseIO;
import org.ribak.nosql.IDatabaseTools;
import org.ribak.nosql.transactions.Contains;
import org.ribak.nosql.transactions.CountKeys;
import org.ribak.nosql.transactions.Delete;
import org.ribak.nosql.transactions.Destroy;
import org.ribak.nosql.transactions.Get;
import org.ribak.nosql.transactions.GetAll;
import org.ribak.nosql.transactions.GetKeys;
import org.ribak.nosql.transactions.Insert;
import org.ribak.nosql.utils.DbKey;
import org.ribak.nosql.utils.PriorityPoolExecutor;
import org.ribak.nosql.utils.exceptions.InitializationException;

import java.util.concurrent.ExecutorService;

/**
 * Created by nribak on 16/11/2016.
 */

public class SnappyDatabase implements IDatabaseIO, IDatabaseTools
{
    private static String sRootDirectory;
    private static ExecutorService sExecutorService;

    private static Kryo sKryo;
    private DB db;
    private String mModuleName;
    private boolean dead;
    public static void init(Context context)
    {
        sRootDirectory = context.getFilesDir().getAbsolutePath();
        sExecutorService = new PriorityPoolExecutor();
        sKryo = new Kryo();
        sKryo.setDefaultSerializer(CompatibleFieldSerializer.class);
    }

    /**
     * creates a new database
     * @param moduleName the module name for the database.
     * @throws InitializationException if {@link SnappyDatabase#init(Context)} was not called first
     */
    SnappyDatabase(String moduleName)
    {
        if(sRootDirectory == null)
            throw new InitializationException("init() was never called");
        this.mModuleName = moduleName;
        if(db != null)
                throw new InitializationException("DB is already open");
        dead = false;
    }

    /**
     * Opens a new connection to the database
     */
    @Override
    public void open()
    {
        try
        {
            db = DBFactory.open(sRootDirectory, mModuleName, sKryo);
        } catch (SnappydbException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Check if this DB is open
     * @return true if the DB is open
     */
    @Override
    public boolean isOpen()
    {
        try
        {
            return db != null && db.isOpen();
        } catch (SnappydbException e)
        {
            return false;
        }
    }

    /**
     * Close the connection to the database only if it is open now
     */
    @Override
    public void close()
    {
        try
        {
            if(db != null && db.isOpen())
                db.close();
        } catch (SnappydbException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public ExecutorService getExecutor() {
        return sExecutorService;
    }

    @Override
    public String getModule() {
        return mModuleName;
    }

    @Override
    public Object getDirectDb() {
        return db;
    }

    @Override
    public void markDead() {
        dead = true;
    }

    @Override
    public boolean isDead() {
        return dead;
    }

    /**
     * Inserts new entry to the database
     * @param key the unique key
     * @param value any object to save (must have a non-args constructor)
     * @return the transaction to use
     */
    public <T> Insert<T> insert(DbKey key, T value)
    {
        return new Insert<>(this, key, value);
    }

    /**
     * Inserts new entry to the database
     * @param group the group or {@code null} to use {@link DbKey#GLOBAL_PREFIX}
     * @param key the unique key in the group
     * @param value any object to save (must have a non-args constructor)
     * @return the transaction to use
     */
    public <T> Insert<T> insert(String group, String key, T value)
    {
        return this.insert(new DbKey(group, key), value);
    }

    /**
     * Inserts new entry to the database in the {@link DbKey#GLOBAL_PREFIX}
     * @param key the unique key in the group
     * @param value any object to save (must have a non-args constructor)
     * @return the transaction to use
     */
    public <T> Insert<T> insert(String key, T value)
    {
        return this.insert(new DbKey(key), value);
    }

    /**
     * Retrieves an entry from the database
     * @param key the unique key
     * @param defaultValue value to return if no key is found in that group (null-ok)
     * @return the transaction to use
     */
    public <T> Get<T> get(DbKey key, T defaultValue)
    {
        return new Get<>(this, key, defaultValue);
    }

    /**
     * Retrieves an entry from the database
     * @param group the group or {@code null} to use {@link DbKey#GLOBAL_PREFIX}
     * @param key the unique key in the group
     * @param defaultValue value to return if no key is found in that group (null-ok)
     * @return the transaction to use
     */
    public <T> Get<T> get(String group, String key, T defaultValue)
    {
        return this.get(new DbKey(group, key), defaultValue);
    }

    /**
     * Retrieves an entry from the default group in the database: {@link DbKey#GLOBAL_PREFIX}
     * @param key the unique key in the group
     * @param defaultValue value to return if no key is found in that group (null-ok)
     * @return the transaction to use
     */
    public <T> Get<T> get(String key, T defaultValue)
    {
        return this.get(new DbKey(key), defaultValue);
    }
    /**
     * Retrieves an entry from the database
     * @param group the group or {@code null} to use {@link DbKey#GLOBAL_PREFIX}
     * @param key the unique key in the group
     * @return the transaction to use
     */
    public <T> Get<T> get(String group, String key)
    {
        return this.get(new DbKey(group, key), null);
    }
    /**
     * Retrieves an entry from the default group in the database: {@link DbKey#GLOBAL_PREFIX}
     * @param key the unique key in the group
     * @return the transaction to use
     */
    public <T> Get<T> get(String key)
    {
        return this.get(new DbKey(key), null);
    }

    /**
     * Deletes an entry from the database
     * @param key the unique key
     * @return the transaction to use
     */
    public Delete delete(DbKey key)
    {
        return new Delete(this, key);
    }

    /**
     * Deletes an entry from the database
     * @param key the unique key
     * @return the transaction to use
     */
    public Delete delete(String group, String key)
    {
        return this.delete(new DbKey(group, key));
    }

    /**
     * Deletes an entry from the database in {@link DbKey#GLOBAL_PREFIX}
     * @param key the unique key in the group
     * @return the transaction to use
     */
    public Delete delete(String key)
    {
        return this.delete(new DbKey(null, key));
    }

    /**
     * Counts the number of entries in a group
     * @param key the keys with the groups to check
     * @return the transaction to use
     */
    public CountKeys count(DbKey key)
    {
        return new CountKeys(this, key);
    }

    /**
     * Counts the number of entries in a group
     * @param group the group to count the entries in
     * @return the transaction to use
     */
    public CountKeys count(@NonNull String group)
    {
        return this.count(new DbKey(group));
    }

    /**
     * Counts the number of entries in all groups in this module
     * @return the transaction to use
     */
    public CountKeys countAll()
    {
        return new CountKeys(this, null);
    }

    /**
     * Retrieves all the keys in a specific group
     * @param dbKey the group to query
     * @return the transaction to use
     */
    public GetKeys getKeys(DbKey dbKey)
    {
        return new GetKeys(this, dbKey);
    }

    /**
     * Retrieves all the keys in a specific group
     * @param group the group to query
     * @return the transaction to use
     */
    public GetKeys getKeys(@NonNull String group)
    {
        return this.getKeys(new DbKey(group));
    }

    /**
     * Retrieves all the keys from all the groups in this module
     * @return the transaction to use
     */
    public GetKeys getAllKeys()
    {
        return new GetKeys(this, null);
    }


    /**
     * retrieves all the values in a specified groups
     * @param key groups to check
     * @return the transaction to use
     */
    public GetAll getAll(DbKey key)
    {
        return new GetAll(this, key);
    }

    /**
     * retrieves all the values in a specific group
     * @param group the group to query
     * @return the transaction to use
     */
    public GetAll getAll(String group)
    {
        return this.getAll(new DbKey(group));
    }

    /**
     * Check if a key exists in a specific group
     * @param group the group to query
     * @param key the key to query
     * @return the transaction to use
     */
    public Contains contains(String group, String key)
    {
        return new Contains(this, new DbKey(group, key));
    }

    /**
     * set a destroy transaction. after this transaction is called, this db is no longer usable
     * @return the transaction to use
     */
    public Destroy destroy() {
        return new Destroy(this);
    }

}
