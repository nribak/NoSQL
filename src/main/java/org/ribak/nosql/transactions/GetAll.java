package org.ribak.nosql.transactions;

import org.ribak.nosql.IDatabaseTools;
import org.ribak.nosql.utils.DbKey;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nribak on 24/11/2016.
 */

public class GetAll extends AbstractTransaction<Void, Map<String, ?>> {

    private boolean getAll;
    public GetAll(IDatabaseTools tools, DbKey key) {
        super(tools, key, null);
        this.getAll = (key == null);
    }

    @Override
    protected Map<String, ?> performTransaction(DbKey dbKey) {
        String[] keys = (getAll) ? getDB().getKeys() : getDB().getKeys(dbKey.getQualifiedGroups());
        Map<String, Object> map = new HashMap<>();
        for (String key : keys) {
            try {
                Object object = getDB().get(key);
                if(object != null)
                    map.put(key, object);
            } catch (IOException e) {
                log(e);
            }
        }
        return map;
    }
}
