package org.ribak.nosql.transactions;

import org.ribak.nosql.db.INoSQLDatabaseTools;

import java.io.IOException;

/**
 * Created by nribak on 21/02/2017.
 */

public class InsertBatch <PARAM> extends AbstractTransaction<PARAM[], Boolean> {

    public InsertBatch(INoSQLDatabaseTools databaseTools, String key, PARAM[] params) {
        super(databaseTools, key, params);
    }

    @Override
    protected Boolean performTransaction(String key) {
        Object[] objects = getParam();
        try {
            api().putBatch(key, objects);
            return true;
        } catch (IOException e) {
            log(e);
            return false;
        }
    }
}
