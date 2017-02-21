package org.ribak.nosql.db;

import android.support.annotation.NonNull;

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

import java.util.concurrent.ExecutorService;

/**
 * Created by nribak on 21/02/2017.
 */

public class KryoDatabase implements IDatabaseTools {
    private static ExecutorService executorService;
    private KDB db;
    private String moduleName;

    {
        executorService = new PriorityPoolExecutor();
    }

    public KryoDatabase(String moduleName) {
        this.moduleName = moduleName;
        this.db = new KDB(moduleName);
    }

    @Override
    public ExecutorService getExecutor() {
        return executorService;
    }

    @Override
    public String getModule() {
        return moduleName;
    }

    @Override
    public Object getDirectDb() {
        return db;
    }

    // TRANSACTIONS //

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
     * @param group the group or {@code null} without group
     * @param key the unique key in the group
     * @param value any object to save (must have a non-args constructor)
     * @return the transaction to use
     */
    public <T> Insert<T> insert(String group, String key, T value)
    {
        return this.insert(new DbKey(group, key), value);
    }

    /**
     * Inserts new entry to the database with no group
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
     * @param group the group or {@code null}
     * @param key the unique key in the group
     * @param defaultValue value to return if no key is found in that group (null-ok)
     * @return the transaction to use
     */
    public <T> Get<T> get(String group, String key, T defaultValue)
    {
        return this.get(new DbKey(group, key), defaultValue);
    }

    /**
     * Retrieves an entry from the default group in the database
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
     * @param group the group or {@code null}
     * @param key the unique key in the group
     * @return the transaction to use
     */
    public <T> Get<T> get(String group, String key)
    {
        return this.get(new DbKey(group, key), null);
    }
    /**
     * Retrieves an entry from the default group in the database
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
     * Deletes an entry from the database
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

    public GetAll getAll() {
        return this.getAll((DbKey) null);
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
