package org.ribak.nosql.transactions;

import com.snappydb.SnappydbException;

import org.ribak.nosql.IDatabaseTools;
import org.ribak.nosql.utils.DbKey;

/**
 * Created by nribak on 16/11/2016.
 */

public class Contains extends AbstractTransaction<Void, Boolean>
{
    public Contains(IDatabaseTools tools, DbKey key)
    {
        super(tools, key, null);
    }

    @Override
    protected Boolean performTransaction(DbKey snappyKey)
    {
        try
        {
            return getDB().exists(snappyKey.getQualifiedKey());
        } catch (SnappydbException e)
        {
            log(e);
        }
        return false;
    }
}
