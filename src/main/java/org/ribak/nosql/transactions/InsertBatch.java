package org.ribak.nosql.transactions;

import org.ribak.nosql.IDatabaseTools;
import org.ribak.nosql.utils.DbKey;

import java.io.IOException;

/**
 * Created by nribak on 21/02/2017.
 */

public class InsertBatch <PARAM> extends AbstractTransaction<PARAM[], Boolean> {

    public InsertBatch(IDatabaseTools databaseTools, DbKey key, PARAM[] params) {
        super(databaseTools, key, params);
    }

    @Override
    protected Boolean performTransaction(DbKey dbKey) {
        String key = dbKey.getQualifiedKey();
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
