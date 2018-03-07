package org.ribak.nosql.transactions;

import org.ribak.nosql.db.IDatabaseTools;

import java.util.Set;

/**
 * Created by nribak on 16/11/2016.
 */

public class CountKeys extends AbstractMultiGetTransaction<Integer>
{
    public CountKeys(IDatabaseTools databaseTools, String prefix) {
        super(databaseTools, prefix);
    }

    @Override
    protected Integer performTransactionWithPrefix(String prefix) {
        Set<String> keys = api().getKeys(prefix);
        return keys.size();
    }
}
