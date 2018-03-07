package org.ribak.nosql.transactions;

import org.ribak.nosql.db.IDatabaseTools;

/**
 * Created by nribak on 16/11/2016.
 */

public class Delete extends AbstractTransaction<Void, Boolean>
{
    public Delete(IDatabaseTools tools, String key)
    {
        super(tools, key, null);
    }

    @Override
    protected Boolean performTransaction(String key)
    {
        return api().delete(key);
    }
}
