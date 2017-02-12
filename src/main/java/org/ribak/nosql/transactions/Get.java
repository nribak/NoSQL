package org.ribak.nosql.transactions;

import com.snappydb.SnappydbException;

import org.ribak.nosql.IDatabaseTools;
import org.ribak.nosql.utils.DbKey;
import org.ribak.nosql.utils.SnappyObject;

/**
 * Created by nribak on 16/11/2016.
 */

public class Get <RESULT> extends AbstractTransaction<Void, RESULT>
{
    private RESULT defaultValue;
    public Get(IDatabaseTools tools, DbKey key, RESULT defaultValue)
    {
        super(tools, key, null);
        this.defaultValue = defaultValue;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected RESULT performTransaction(DbKey dbKey)
    {
        try
        {
            SnappyObject<RESULT> snappyObject = getDB().getObject(dbKey.getQualifiedKey(), SnappyObject.class);
            if(snappyObject != null)
                return snappyObject.getObject();

        } catch (SnappydbException e)
        {
            log(e);
        }
        return defaultValue;
    }

}
