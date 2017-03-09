package org.ribak.nosql.transactions;

import org.ribak.nosql.IDatabaseTools;

/**
 * Created by nribak on 16/11/2016.
 */

public class Contains extends AbstractTransaction<Void, Boolean>
{
    public Contains(IDatabaseTools tools, String key)
    {
        super(tools, key, null);
    }

    @Override
    protected Boolean performTransaction(String key)
    {
        return getDB().has(key);
    }
}
