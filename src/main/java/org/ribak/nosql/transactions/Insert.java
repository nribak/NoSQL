package org.ribak.nosql.transactions;

import org.ribak.nosql.db.INoSQLDatabaseTools;

import java.io.IOException;

/**
 * Created by nribak on 16/11/2016.
 */

public class Insert<PARAM> extends AbstractTransaction<PARAM, Boolean>
{
    public Insert(INoSQLDatabaseTools tools, String key, PARAM param)
    {
        super(tools, key, param);
    }

    @Override
    protected Boolean performTransaction(String key)
    {
        try {
            api().put(key, getParam());
            return true;
        } catch (IOException e) {
            log(e);
            return false;
        }
    }

}
