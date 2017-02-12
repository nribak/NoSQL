package org.ribak.nosql.transactions;

import com.snappydb.SnappydbException;

import org.ribak.nosql.IDatabaseTools;
import org.ribak.nosql.utils.DbKey;

/**
 * Created by nribak on 16/11/2016.
 */

public class Delete extends AbstractTransaction<Void, Boolean>
{
    public Delete(IDatabaseTools tools, DbKey key)
    {
        super(tools, key, null);
    }

    @Override
    protected Boolean performTransaction(DbKey dbKey)
    {
        try
        {
            getDB().del(dbKey.getQualifiedKey());
            return true;
        } catch (SnappydbException e)
        {
            log(e);
        }
        return false;
    }
}
