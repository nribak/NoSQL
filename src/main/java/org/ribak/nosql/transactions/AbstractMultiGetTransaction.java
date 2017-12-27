package org.ribak.nosql.transactions;

import org.ribak.nosql.db.IDatabaseTools;

/**
 * Created by nribak on 22/02/2017.
 */

abstract class AbstractMultiGetTransaction<RESULT> extends AbstractTransaction<Void, RESULT> {
    private String prefix;
    AbstractMultiGetTransaction(IDatabaseTools databaseTools, String prefix) {
        super(databaseTools, null, null);
        this.prefix = prefix;
    }

    @Override
    protected RESULT performTransaction(String key) {
        return performTransactionWithPrefix(prefix);
    }

    protected abstract RESULT performTransactionWithPrefix(String prefix);

}
