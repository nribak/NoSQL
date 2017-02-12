package org.ribak.nosql.transactions;

import com.snappydb.SnappydbException;

import org.ribak.nosql.IDatabaseTools;
import org.ribak.nosql.utils.DbKey;
import org.ribak.nosql.utils.SnappyObject;

/**
 * Created by nribak on 16/11/2016.
 */

public class Insert<PARAM> extends AbstractTransaction<PARAM, Boolean>
{
    public Insert(IDatabaseTools tools, DbKey key, PARAM param)
    {
        super(tools, key, param);
    }

    @Override
    protected Boolean performTransaction(DbKey dbKey)
    {
        SnappyObject<PARAM> object = new SnappyObject<>(getParam());
        try
        {
            getDB().put(dbKey.getQualifiedKey(), object);
            return true;
        } catch (SnappydbException e)
        {
            log(e);
        }
        return false;
    }

}
