package org.ribak.nosql.transactions;

import org.ribak.nosql.IDatabaseTools;

/**
 * Created by nribak on 16/11/2016.
 */

public class GetKeys extends AbstractMultiGetTransaction<String[]> {
    public GetKeys(IDatabaseTools databaseTools, String prefix) {
        super(databaseTools, prefix);
    }

    @Override
    protected String[] performTransactionWithPrefix(String prefix) {
        return getDB().getKeys(prefix);
    }
}
