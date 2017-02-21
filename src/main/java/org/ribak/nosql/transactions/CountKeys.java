package org.ribak.nosql.transactions;

import org.ribak.nosql.IDatabaseTools;
import org.ribak.nosql.utils.DbKey;

/**
 * Created by nribak on 16/11/2016.
 */

public class CountKeys extends AbstractTransaction<Void, Integer>
{
    private boolean countAll;
    public CountKeys(IDatabaseTools tools, DbKey dbKey)
    {
        super(tools, dbKey, null);
        this.countAll = dbKey == null;
    }

    @Override
    protected Integer performTransaction(DbKey dbKey)
    {
        String[] keys = (countAll) ?  getDB().getKeys() : getDB().getKeys(dbKey.getQualifiedGroups());
        return keys.length;
    }
}
