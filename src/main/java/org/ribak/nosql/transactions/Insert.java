package org.ribak.nosql.transactions;

import org.ribak.nosql.IDatabaseTools;
import org.ribak.nosql.utils.DbKey;

import java.io.IOException;

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
        try {
            getDB().put(dbKey.getQualifiedKey(), getParam());
            return true;
        } catch (IOException e) {
            log(e);
            return false;
        }
    }

}
