package org.ribak.nosql.transactions;

import com.snappydb.SnappydbException;

import org.ribak.nosql.IDatabaseTools;
import org.ribak.nosql.utils.DbKey;

/**
 * Created by nribak on 12/02/2017.
 */

public class Destroy extends AbstractTransaction<Void, Boolean> {
    public Destroy(IDatabaseTools tools) {
        super(tools, null, null);
    }

    @Override
    protected Boolean performTransaction(DbKey k) {
        try {
            getDB().destroy();
            setDatabaseDead();
            return true;
        } catch (SnappydbException e) {
            log(e);
            return false;
        }
    }
}
