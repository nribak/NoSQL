package org.ribak.nosql.transactions;

import org.ribak.nosql.db.INoSQLDatabaseTools;

/**
 * Created by nribak on 16/11/2016.
 */

public class Contains extends AbstractTransaction<Void, Boolean>
{
    public Contains(INoSQLDatabaseTools tools, String key)
    {
        super(tools, key, null);
    }

    @Override
    protected Boolean performTransaction(String key)
    {
        return api().contains(key);
    }
}
