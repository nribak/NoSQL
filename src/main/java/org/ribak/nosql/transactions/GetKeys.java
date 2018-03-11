package org.ribak.nosql.transactions;

import org.ribak.nosql.db.INoSQLDatabaseTools;

import java.util.Set;

/**
 * Created by nribak on 16/11/2016.
 */

public class GetKeys extends AbstractMultiGetTransaction<String[]> {
    public GetKeys(INoSQLDatabaseTools databaseTools, String prefix) {
        super(databaseTools, prefix);
    }

    @Override
    protected String[] performTransactionWithPrefix(String prefix) {
        Set<String> keys = api().getKeys(prefix);
        return keys.toArray(new String[keys.size()]);
    }
}
