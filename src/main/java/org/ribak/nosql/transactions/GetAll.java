package org.ribak.nosql.transactions;

import org.ribak.nosql.db.IDatabaseTools;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nribak on 24/11/2016.
 */

public class GetAll extends AbstractMultiGetTransaction<Map<String, ?>> {
    public GetAll(IDatabaseTools databaseTools, String prefix) {
        super(databaseTools, prefix);
    }

    @Override
    protected Map<String, ?> performTransactionWithPrefix(String prefix) {
        String[] keys = getDB().getKeys(prefix);
        Map<String, Object> map = new HashMap<>();
        for (String key : keys) {
            try {
                Object object = getDB().get(key);
                if (object != null)
                    map.put(key, object);
            } catch (IOException e) {
                log(e);
            }
        }
        return map;
    }
}
