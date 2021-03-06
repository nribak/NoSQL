package org.ribak.nosql.transactions;

import org.ribak.nosql.db.INoSQLDatabaseTools;

/**
 * Created by nribak on 16/11/2016.
 */

public class Delete extends AbstractTransaction<Void, Boolean>
{
    public Delete(INoSQLDatabaseTools tools, String key)
    {
        super(tools, key, null);
    }

    @Override
    protected Boolean performTransaction(String key)
    {
        return api().delete(key);
    }
}
