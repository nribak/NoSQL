package org.ribak.nosql.db;

import org.ribak.nosql.transactions.Contains;
import org.ribak.nosql.transactions.CountKeys;
import org.ribak.nosql.transactions.Delete;
import org.ribak.nosql.transactions.Destroy;
import org.ribak.nosql.transactions.Get;
import org.ribak.nosql.transactions.GetAll;
import org.ribak.nosql.transactions.GetBatch;
import org.ribak.nosql.transactions.GetKeys;
import org.ribak.nosql.transactions.Insert;
import org.ribak.nosql.transactions.InsertBatch;
import org.ribak.nosql.utils.PriorityPoolExecutor;

import java.util.concurrent.ExecutorService;

/**
 * Created by nribak on 21/02/2017.
 */

public class KryoDatabase implements IDatabaseTools {
    private static ExecutorService executorService;
    private DatabaseAPI api;
    private String moduleName;

    static {
        executorService = new PriorityPoolExecutor();
    }

    public KryoDatabase(String moduleName) {
        this.moduleName = moduleName;
        this.api = new KryoDatabaseAPI(moduleName);
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
    public DatabaseAPI getDirectDb() {
        return api;
    }

    // TRANSACTIONS //
    /**
     * Inserts new entry to the database
     * @param key the unique key in the group
     * @param value any object to save (must have a non-args constructor)
     * @return the transaction to use
     */
    public <T> Insert<T> insert(String key, T value)
    {
        return new Insert<>(this, key, value);
    }

    /**
     * Retrieves an entry from the default group in the database
     * @param key the unique key in the group
     * @param defaultValue value to return if no key is found in that group (null-ok)
     * @return the transaction to use
     */
    public <T> Get<T> get(String key, T defaultValue)
    {
        return new Get<>(this, key, defaultValue);
    }
    /**
     * Retrieves an entry from the default group in the database
     * @param key the unique key in the group
     * @return the transaction to use
     */
    public <T> Get<T> get(String key)
    {
        return this.get(key, null);
    }

    /**
     * Deletes an entry from the database
     * @param key the unique key in the group
     * @return the transaction to use
     */
    public Delete delete(String key)
    {
        return new Delete(this, key);
    }

    public CountKeys count(String prefix) {
        return new CountKeys(this, prefix);
    }

    public CountKeys count() {
        return this.count(null);
    }
    public GetKeys getKeys(String prefix) {
        return new GetKeys(this, prefix);
    }

    public GetKeys getKeys() {
        return this.getKeys(null);
    }
    /**
     * retrieves all the values
     * @return the transaction to use
     */
    public GetAll getAll(String prefix)
    {
        return new GetAll(this, prefix);
    }

    /**
     * retrieves all the values
     * @return the transaction to use
     */
    public GetAll getAll()
    {
        return this.getAll(null);
    }

    /**
     * Check if a key exists in a specific group
     * @param key the key to query
     * @return the transaction to use
     */
    public Contains contains(String key)
    {
        return new Contains(this, key);
    }

    /**
     * set a destroy transaction. after this transaction is called, this db is no longer usable
     * @return the transaction to use
     */
    public Destroy destroy() {
        return new Destroy(this);
    }

    public <T> InsertBatch<T> insertArray(String key, T[] array) {
        return new InsertBatch<>(this, key, array);
    }

    public <T> GetBatch<T> getArray(String key) {
        return new GetBatch<>(this, key);
    }

}
