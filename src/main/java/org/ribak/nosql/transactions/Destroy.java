package org.ribak.nosql.transactions;

import org.ribak.nosql.db.INoSQLDatabaseTools;

/**
 * Created by nribak on 12/02/2017.
 */

public class Destroy extends AbstractTransaction<Void, Boolean> {
    public Destroy(INoSQLDatabaseTools tools) {
        super(tools, null, null);
    }

    @Override
    protected Boolean performTransaction(String k) {
        return api().destroy();
    }
}
