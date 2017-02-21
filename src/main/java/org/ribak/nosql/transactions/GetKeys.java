package org.ribak.nosql.transactions;

import org.ribak.nosql.IDatabaseTools;
import org.ribak.nosql.utils.DbKey;

/**
 * Created by nribak on 16/11/2016.
 */

public class GetKeys extends AbstractTransaction<Void, String[]>
{
    private boolean getAll;
    public GetKeys(IDatabaseTools tools, DbKey key)
    {
        super(tools, key, null);
        this.getAll = (key == null);
    }

    @Override
    protected String[] performTransaction(DbKey dbKey)
    {
        return (getAll) ? getDB().getKeys() : getDB().getKeys(dbKey.getQualifiedGroups());
    }
}
