package org.ribak.nosql.transactions;

import org.ribak.nosql.db.IDatabaseTools;

import java.io.IOException;

/**
 * Created by nribak on 16/11/2016.
 */

public class Get <RESULT> extends AbstractTransaction<Void, RESULT>
{
    private RESULT defaultValue;
    public Get(IDatabaseTools tools, String key, RESULT defaultValue)
    {
        super(tools, key, null);
        this.defaultValue = defaultValue;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected RESULT performTransaction(String key)
    {
        try {
            return (RESULT) api().get(key);
        } catch (IOException e) {
            log(e);
            return defaultValue;
        }
    }

}
