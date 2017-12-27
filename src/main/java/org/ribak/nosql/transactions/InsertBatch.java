package org.ribak.nosql.transactions;

import org.ribak.nosql.db.IDatabaseTools;

import java.io.IOException;

/**
 * Created by nribak on 21/02/2017.
 */

public class InsertBatch <PARAM> extends AbstractTransaction<PARAM[], Boolean> {

    public InsertBatch(IDatabaseTools databaseTools, String key, PARAM[] params) {
        super(databaseTools, key, params);
    }

    @Override
    protected Boolean performTransaction(String key) {
        Object[] objects = getParam();
        try {
            getDB().putBatch(key, objects);
            return true;
        } catch (IOException e) {
            log(e);
            return false;
        }
    }
}
