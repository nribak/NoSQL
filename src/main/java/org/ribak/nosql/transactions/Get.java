package org.ribak.nosql.transactions;

import org.ribak.nosql.IDatabaseTools;
import org.ribak.nosql.utils.DbKey;

import java.io.IOException;

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
        try {
            return (RESULT) getDB().get(dbKey.getQualifiedKey());
        } catch (IOException e) {
            log(e);
            return defaultValue;
        }
    }

}
