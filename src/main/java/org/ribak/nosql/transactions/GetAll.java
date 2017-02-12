package org.ribak.nosql.transactions;

import com.snappydb.SnappydbException;

import org.ribak.nosql.IDatabaseTools;
import org.ribak.nosql.utils.DbKey;
import org.ribak.nosql.utils.SnappyObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nribak on 24/11/2016.
 */

public class GetAll extends AbstractTransaction<Void, Map<DbKey, ?>> {

    private boolean getAll;
    public GetAll(IDatabaseTools tools, DbKey key) {
        super(tools, key, null);
        this.getAll = (key == null);
    }

    @Override
    protected Map<DbKey, ?> performTransaction(DbKey dbKey) {
        Map<DbKey, Object> map = new HashMap<>();
        String prefix = (getAll) ? DbKey.GLOBAL_PREFIX : dbKey.getQualifiedGroups(true);
        try {
            String[] keys = getDB().findKeys(prefix);
            for (String key : keys) {
                DbKey k = DbKey.create(key);
                if(k != null) {
                    SnappyObject<?> object = getDB().getObject(key, SnappyObject.class);
                    if(object != null && !object.isObjectNull())
                        map.put(k, object.getObject());
                }

            }
        } catch (SnappydbException e) {
            log(e);
        }
        return map;
    }
}
